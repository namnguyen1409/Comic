package top.telecomic.authservice.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import top.telecomic.authservice.exception.ErrorCode;
import top.telecomic.authservice.exception.GlobalException;
import top.telecomic.authservice.service.JwtService;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtServiceImpl implements JwtService {

    JwtDecoder jwtDecoder;

    @Override
    public Jwt parseToken(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            log.error("Failed to decode JWT token: {}", token, e);
            throw new GlobalException(ErrorCode.INVALID_JWT);
        }
    }

}
