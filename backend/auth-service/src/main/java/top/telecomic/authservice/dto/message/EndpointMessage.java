package top.telecomic.authservice.dto.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * DTO for {@link top.telecomic.authservice.entity.Endpoint}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EndpointMessage(String serviceName, String path, String method) implements Serializable {
}