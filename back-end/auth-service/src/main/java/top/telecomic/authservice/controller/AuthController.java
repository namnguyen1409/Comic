package top.telecomic.authservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.telecomic.authservice.dto.request.LoginRequest;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoint for authentication")
public class AuthController {


    @PostMapping("/login")
    public void login(
            @RequestBody LoginRequest request
    ) {

    }

}
