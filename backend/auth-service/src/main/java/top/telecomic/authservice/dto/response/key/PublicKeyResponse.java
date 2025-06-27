package top.telecomic.authservice.dto.response.key;

import top.telecomic.authservice.enums.CryptoAlgorithm;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link top.telecomic.authservice.entity.Key}
 */
public record PublicKeyResponse(
        UUID keyId,
        CryptoAlgorithm algorithm,
        String publicKey,
        Boolean isActive
) implements Serializable {
}