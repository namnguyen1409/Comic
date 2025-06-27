package top.telecomic.authservice.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link top.telecomic.authservice.entity.Permission}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PermissionResponse(Instant createdAt, String createdBy, Instant updatedAt, String updatedBy, Boolean deleted, String code, String name, String description) implements Serializable {
  }