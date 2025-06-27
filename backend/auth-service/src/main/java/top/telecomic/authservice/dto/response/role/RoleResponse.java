package top.telecomic.authservice.dto.response.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import top.telecomic.authservice.dto.response.permission.PermissionResponse;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link top.telecomic.authservice.entity.Role}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RoleResponse(String code, String name, String description,
                           Set<PermissionResponse> permissions) implements Serializable {
}