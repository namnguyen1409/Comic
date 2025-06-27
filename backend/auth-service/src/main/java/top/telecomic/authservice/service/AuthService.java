package top.telecomic.authservice.service;

import top.telecomic.authservice.dto.request.auth.LoginRequest;
import top.telecomic.authservice.dto.response.auth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
