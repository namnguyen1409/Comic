package top.telecomic.authservice.dto.response.auth;

import java.io.Serializable;
import java.time.Instant;

public record LoginResponse(
        String token,
        Instant expiresAt
) implements Serializable {
}
