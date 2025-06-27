package top.telecomic.authservice.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * DTO for {@link top.telecomic.authservice.entity.Permission}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PermissionResponse(String code,
                                 String name,
                                 String description
) implements Serializable {
}