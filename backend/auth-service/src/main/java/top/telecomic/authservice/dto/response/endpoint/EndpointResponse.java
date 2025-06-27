package top.telecomic.authservice.dto.response.endpoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import top.telecomic.authservice.dto.response.permission.PermissionResponse;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link top.telecomic.authservice.entity.Endpoint}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EndpointResponse(String serviceName, String path, String method, String description,
                               Set<PermissionResponse> permissions, Boolean isPublic) implements Serializable {
}