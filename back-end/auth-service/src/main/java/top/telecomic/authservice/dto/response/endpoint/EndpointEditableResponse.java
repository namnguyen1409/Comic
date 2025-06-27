package top.telecomic.authservice.dto.response.endpoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import top.telecomic.authservice.dto.response.permission.PermissionResponse;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link top.telecomic.authservice.entity.Endpoint}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EndpointEditableResponse(UUID id, Boolean deleted, String serviceName, String path, String method, String description, Set<PermissionResponse> permissions, Boolean isPublic) implements Serializable {
  }