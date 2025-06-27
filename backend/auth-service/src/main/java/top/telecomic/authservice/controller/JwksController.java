package top.telecomic.authservice.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.telecomic.authservice.service.JWKService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "JWKS", description = "Endpoint for retrieving JSON Web Key Set")
@RequestMapping("/auth/jwks")
public class JwksController {

    JWKService jWKService;

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> getKeys() {
        return jWKService.getKeys();
    }
}
