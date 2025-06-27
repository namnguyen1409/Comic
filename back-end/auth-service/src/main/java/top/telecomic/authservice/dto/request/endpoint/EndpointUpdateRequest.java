package top.telecomic.authservice.dto.request.endpoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link top.telecomic.authservice.entity.Endpoint}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndpointUpdateRequest implements Serializable {
    String description;
    Set<String> permissionCodes;
    Boolean isPublic;
}