package top.telecomic.authservice.dto.request.endpoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.util.Set;

/**
 * DTO for {@link top.telecomic.authservice.entity.Endpoint}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EndpointUpdateRequest(String description, Set<String> permissionCodes, Boolean isPublic) {
}