package top.telecomic.authservice.service;

import org.springframework.data.domain.Page;
import top.telecomic.authservice.dto.filter.endpoint.EndpointFilter;
import top.telecomic.authservice.dto.message.EndpointMessage;
import top.telecomic.authservice.dto.request.endpoint.EndpointUpdateRequest;
import top.telecomic.authservice.dto.response.endpoint.EndpointDetailResponse;
import top.telecomic.authservice.dto.response.endpoint.EndpointEditableResponse;
import top.telecomic.authservice.dto.response.endpoint.EndpointResponse;

import java.util.List;

public interface EndpointService {
    void addEndpoints(List<EndpointMessage> endpoints);

    List<EndpointResponse> getAllEndpoints();

    void invalidateCacheAllEndpoints();

    List<EndpointResponse> getPublicEndpoints();

    Page<EndpointEditableResponse> getEditableEndpoints(EndpointFilter endpointFilter);

    EndpointDetailResponse getEndpointById(String id);

    void updateEndpoint(String id, EndpointUpdateRequest endpointUpdateRequest);
}
