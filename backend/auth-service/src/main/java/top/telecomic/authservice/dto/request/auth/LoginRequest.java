package top.telecomic.authservice.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginRequest(
        @NotNull(message = "Username cannot be null")
        String username,
        @NotNull(message = "Password cannot be null")
        String password
) implements Serializable {

}
