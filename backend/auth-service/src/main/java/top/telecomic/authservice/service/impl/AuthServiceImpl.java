package top.telecomic.authservice.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.telecomic.authservice.dto.request.auth.LoginRequest;
import top.telecomic.authservice.dto.response.auth.LoginResponse;
import top.telecomic.authservice.entity.Device;
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
import top.telecomic.authservice.service.JwtService;
import top.telecomic.authservice.service.TokenService;
import top.telecomic.authservice.utils.CookiesUtils;
import top.telecomic.authservice.utils.SecurityUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

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
    JwtService jwtService;
    private final TokenService tokenService;

    @NonFinal
    @Value("${security.key.jwt.access-token.time-to-live}")
    Duration accessTokenTtl;

    @NonFinal
    @Value("${security.key.jwt.short-lived-access-token.time-to-live}")
    Duration shortLivedAccessTokenTtl;


    @NonFinal
    @Value("${security.key.jwt.refresh-token.time-to-live}")
    Duration refreshTokenTtl;

    @Transactional(noRollbackFor = GlobalException.class)
    @Override
    public LoginResponse login(LoginRequest request) {
        var deviceId = jwtService.parseToken(
                cookiesUtils.getCookie("X-Device-Id").orElseThrow(
                        () -> new GlobalException(ErrorCode.DEVICE_NOT_FOUND)
                )
        ).getId();
        var device = deviceRepository.findById(UUID.fromString(deviceId))
                .orElseThrow(() -> new GlobalException(ErrorCode.DEVICE_NOT_FOUND));

        var authMethod = authenticationMethodRepository.findByUserUsernameAndProvider(
                request.username(), AuthProvider.LOCAL
        ).orElseThrow(() -> new GlobalException(ErrorCode.AUTHENTICATION_METHOD_NOT_FOUND));

        var session = generateLoginSession(authMethod.getUser(), device);

        if (!passwordEncoder.matches(request.password(), authMethod.getPasswordHash())) {
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
            accessToken = tokenService.createAccessToken(session);
            session.setExpiredAt(Instant.now().plus(shortLivedAccessTokenTtl));
        } else {
            session.setStatus(LoginSessionStatus.AUTHENTICATED);
            accessToken = tokenService.createAccessToken(session);
            tokenService.createRefreshToken(session);
            session.setExpiredAt(Instant.now().plus(refreshTokenTtl));
        }
        saveLoginSession(session);
        return new LoginResponse(accessToken, session.getExpiredAt());
    }

    private LoginSession generateLoginSession(User user, Device device) {
        var session = new LoginSession();
        session.setIpAddress(securityUtils.getIpAddress());
        session.setUser(user);
        session.setProvider(AuthProvider.LOCAL);
        session.setDevice(device);
        session.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        return session;
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
