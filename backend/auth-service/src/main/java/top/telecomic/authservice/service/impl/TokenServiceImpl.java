package top.telecomic.authservice.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.telecomic.authservice.entity.LoginSession;
import top.telecomic.authservice.entity.User;
import top.telecomic.authservice.enums.LoginSessionStatus;
import top.telecomic.authservice.exception.ErrorCode;
import top.telecomic.authservice.exception.GlobalException;
import top.telecomic.authservice.service.KeyService;
import top.telecomic.authservice.service.TokenService;
import top.telecomic.authservice.utils.CookiesUtils;
import top.telecomic.authservice.utils.SecurityUtils;

import java.time.Duration;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {


    private final SecurityUtils securityUtils;
    private final KeyService keyService;
    private final CookiesUtils cookiesUtils;
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
    @Value("${security.key.jwt.short-lived-access-token.time-to-live}")
    Duration shortLivedAccessTokenTtl;


    @NonFinal
    @Value("${security.key.jwt.refresh-token.time-to-live}")
    Duration refreshTokenTtl;


    @Override
    public String createAccessToken(LoginSession session) {
        var accessTokenId = UUID.randomUUID();
        var scopeExp = switch (session.getStatus()) {
            case AUTHENTICATED -> new ScopeExp(buildScope(session.getUser()), accessTokenTtl);
            case PENDING_2FA -> new ScopeExp(LoginSessionStatus.PENDING_2FA.name(), shortLivedAccessTokenTtl);
            default -> throw new GlobalException(ErrorCode.INVALID_LOGIN_SESSION_STATUS);
        };
        var claims = Map.of(
                "scope", scopeExp.scope()
        );

        var accessToken = securityUtils.createJwt(
                accessTokenId.toString(),
                session.getUser().getId().toString(),
                claims,
                scopeExp.exp(),
                keyService.getLatestKey()
        );

        session.setAccessTokenId(accessTokenId);
        return accessToken;
    }


    @Override
    public void createRefreshToken(LoginSession session) {
        var refreshTokenId = UUID.randomUUID();
        session.setRefreshTokenId(refreshTokenId);

        var claims = Map.of(
                "sessionId", session.getId().toString(),
                "deviceId", session.getDevice().getId().toString()
        );

        var refreshToken = securityUtils.createJwt(
                refreshTokenId.toString(),
                session.getUser().getId().toString(),
                claims,
                refreshTokenTtl,
                keyService.getLatestKey()
        );

        cookiesUtils.setCookie("X-Refresh-Token", refreshToken, (int) refreshTokenTtl.getSeconds());
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

    private record ScopeExp(String scope, Duration exp) {
    }


}
