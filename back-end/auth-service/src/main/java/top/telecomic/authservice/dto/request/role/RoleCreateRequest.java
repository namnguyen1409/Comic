package top.telecomic.authservice.dto.request.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link top.telecomic.authservice.entity.Role}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RoleCreateRequest(String code, String name, String description, Set<String> permissionCodes) implements Serializable {
  }