package top.telecomic.authservice.dto.response;

import java.io.Serializable;
import java.time.Instant;

public record LoginResponse(
        String token,
        Instant expiresAt
) implements Serializable {
}
