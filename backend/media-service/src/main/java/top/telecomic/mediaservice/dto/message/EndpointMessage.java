package top.telecomic.mediaservice.dto.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public record EndpointMessage(String serviceName, String path, String method) implements Serializable {
}