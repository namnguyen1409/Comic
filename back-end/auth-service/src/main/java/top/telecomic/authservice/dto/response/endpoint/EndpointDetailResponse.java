package top.telecomic.authservice.dto.response.endpoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import top.telecomic.authservice.dto.response.permission.PermissionResponse;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link top.telecomic.authservice.entity.Endpoint}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EndpointDetailResponse(UUID id, Instant createdAt, String createdBy, Instant updatedAt, String updatedBy, Boolean deleted, String serviceName, String path, String method, String description, Set<PermissionResponse> permissions, Boolean isPublic) implements Serializable {
  }