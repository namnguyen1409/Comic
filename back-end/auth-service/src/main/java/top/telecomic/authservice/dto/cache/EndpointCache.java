package top.telecomic.authservice.dto.cache;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link top.telecomic.authservice.entity.Endpoint}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EndpointCache(String serviceName, String path, String method, Set<String> permissionCodes, Boolean isPublic) implements Serializable {
  }