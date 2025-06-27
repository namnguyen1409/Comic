package top.telecomic.authservice.service;

import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtService {
    Jwt parseToken(String token);
}
