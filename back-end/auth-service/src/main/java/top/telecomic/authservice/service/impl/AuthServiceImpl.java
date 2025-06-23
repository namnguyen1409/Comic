package top.telecomic.authservice.service.impl;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.telecomic.authservice.dto.request.LoginRequest;
import top.telecomic.authservice.dto.response.LoginResponse;
import top.telecomic.authservice.entity.LoginSession;
import top.telecomic.authservice.entity.User;
import top.telecomic.authservice.enums.AuthProvider;
import top.telecomic.authservice.enums.LoginSessionStatus;
import top.telecomic.authservice.exception.ErrorCode;
import top.telecomic.authservice.exception.GlobalException;
import top.telecomic.authservice.repository.AuthenticationMethodRepository;
import top.telecomic.authservice.repository.DeviceRepository;
import top.telecomic.authservice.repository.LoginSessionRepository;
import top.telecomic.authservice.repository.UserDeviceRepository;
import top.telecomic.authservice.service.AuthService;
import top.telecomic.authservice.service.KeyService;
import top.telecomic.authservice.utils.CookiesUtils;
import top.telecomic.authservice.utils.SecurityUtils;

import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import static top.telecomic.authservice.utils.KeyUtils.buildSigner;
import static top.telecomic.authservice.utils.KeyUtils.getPrivateKey;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    AuthenticationMethodRepository authenticationMethodRepository;
    BCryptPasswordEncoder passwordEncoder;
    CookiesUtils cookiesUtils;
    UserDeviceRepository userDeviceRepository;
    SecurityUtils securityUtils;
    DeviceRepository deviceRepository;
    HttpServletRequest httpServletRequest;
    LoginSessionRepository loginSessionRepository;
    KeyService keyService;

    @NonFinal
    @Value("${setup.role.prefix}")
    String rolePrefix;

    @NonFinal
    @Value("${spring.security.oauth2.authorizationserver.issuer}")
    String issuer;

    @NonFinal
    @Value("${security.key.jwt.access-token.time-to-live}")
    Duration accessTokenTtl;

    @NonFinal
    @Value("${security.key.jwt.refresh-token.time-to-live}")
    Duration refreshTokenTtl;

    @Transactional(noRollbackFor = GlobalException.class)
    public LoginResponse login(LoginRequest request) {
        var deviceId = cookiesUtils.getCookie("X-Device-Id");
        if (deviceId == null || deviceId.isEmpty()) {
            throw new GlobalException(ErrorCode.DEVICE_NOT_FOUND, "Device not found");
        }

        var device = deviceRepository.findById(UUID.fromString(deviceId))
                .orElseThrow(() -> new GlobalException(ErrorCode.DEVICE_NOT_FOUND));

        var authMethod = authenticationMethodRepository.findByUserUsernameAndProvider(
                request.getUsername(), AuthProvider.LOCAL
        ).orElseThrow(() -> new GlobalException(ErrorCode.AUTHENTICATION_METHOD_NOT_FOUND));

        var session = new LoginSession();
        session.setIpAddress(securityUtils.getIpAddress());
        session.setDevices(device);
        session.setUser(authMethod.getUser());
        session.setProvider(AuthProvider.LOCAL);
        session.setUserAgent(httpServletRequest.getHeader("User-Agent"));

        if (!passwordEncoder.matches(request.getPassword(), authMethod.getPasswordHash())) {
            session.setExpiredAt(Instant.now());
            session.setStatus(LoginSessionStatus.FAILED);
            loginSessionRepository.save(session);
            throw new GlobalException(ErrorCode.INVALID_CREDENTIALS);
        }

        var user = authMethod.getUser();
        var userDevice = userDeviceRepository.findByUserUsernameAndDeviceId(
                user.getUsername(), UUID.fromString(deviceId)
        ).orElse(null);

        boolean is2faRequired = user.getIs2faEnabled() && (userDevice == null || !userDevice.getIsTrusted());

        String accessToken;
        if (is2faRequired) {
            session.setStatus(LoginSessionStatus.PENDING_2FA);
            accessToken = createAccessToken(session);
            session.setExpiredAt(Instant.now().plus(Duration.ofMinutes(5)));
        } else {
            session.setStatus(LoginSessionStatus.AUTHENTICATED);
            accessToken = createAccessToken(session);
            createRefreshToken(session);
            session.setExpiredAt(Instant.now().plus(refreshTokenTtl));
        }

        saveLoginSession(session);
        return new LoginResponse(accessToken, session.getExpiredAt());
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> {
                stringJoiner.add(rolePrefix + role.getCode());
                if (role.getPermissions() != null) {
                    role.getPermissions().forEach(permission -> {
                        if (user.getRevokedPermissions() != null && user.getRevokedPermissions().contains(permission)) {
                            return;
                        }
                        stringJoiner.add(permission.getCode());
                    });
                }
            });
        }
        return stringJoiner.toString();
    }

    private String createAccessToken(LoginSession session) {
        var accessTokenId = UUID.randomUUID();
        var key = keyService.getLatestKey();
        PrivateKey privateKey = getPrivateKey(key);
        JWSHeader header = new JWSHeader(key.getAlgorithm().getName());
        String scope;
        Date exp;
        if (session.getStatus() == LoginSessionStatus.PENDING_2FA) {
            scope = LoginSessionStatus.PENDING_2FA.name();
            exp = Date.from(Instant.now().plus(Duration.ofMinutes(5)));
        } else {
            scope = buildScope(session.getUser());
            exp = Date.from(Instant.now().plus(accessTokenTtl));
        }
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(session.getUser().getId().toString())
                .issuer(issuer)
                .issueTime(new Date())
                .expirationTime(exp)
                .jwtID(accessTokenId.toString())
                .claim("scope", scope)
                .build();
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        JWSSigner signer = buildSigner(key, privateKey);
        try {
            signedJWT.sign(signer);
            session.setAccessTokenId(accessTokenId);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.TOKEN_CREATION_FAILED);
        }
    }

    private void createRefreshToken(LoginSession session) {
        var refreshTokenId = UUID.randomUUID();
        session.setRefreshTokenHash(passwordEncoder.encode(refreshTokenId.toString()));
        cookiesUtils.setCookie("X-Refresh-Token", refreshTokenId.toString(), (int) refreshTokenTtl.getSeconds());
    }

    private void saveLoginSession(LoginSession session) {
        try {
            var sessionId = loginSessionRepository.save(session).getId().toString();
            cookiesUtils.setCookie("X-Session-Id", sessionId, (int) accessTokenTtl.getSeconds());
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to save login session: " + e.getMessage());
        }
    }

}
