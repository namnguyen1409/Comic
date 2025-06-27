package top.telecomic.authservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.telecomic.authservice.dto.request.auth.LoginRequest;
import top.telecomic.authservice.dto.response.CustomApiResponse;
import top.telecomic.authservice.dto.response.auth.LoginResponse;
import top.telecomic.authservice.service.AuthService;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoint for authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public CustomApiResponse<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        var response = authService.login(request);
        return CustomApiResponse.<LoginResponse>builder()
                .message("Login successful")
                .data(response)
                .build();
    }

    @PostMapping("/register")
    public CustomApiResponse<?> register() {


        return null;
    }

}
