package top.telecomic.authservice.service;

import top.telecomic.authservice.entity.LoginSession;

public interface TokenService {
    String createAccessToken(LoginSession session);

    void createRefreshToken(LoginSession session);
}
