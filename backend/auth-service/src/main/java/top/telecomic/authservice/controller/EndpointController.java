package top.telecomic.authservice.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import top.telecomic.authservice.dto.filter.endpoint.EndpointFilter;
import top.telecomic.authservice.dto.request.endpoint.EndpointUpdateRequest;
import top.telecomic.authservice.dto.response.CustomApiResponse;
import top.telecomic.authservice.dto.response.endpoint.EndpointDetailResponse;
import top.telecomic.authservice.dto.response.endpoint.EndpointEditableResponse;
import top.telecomic.authservice.dto.response.endpoint.EndpointResponse;
import top.telecomic.authservice.service.EndpointService;

import java.util.List;

@RestController
@RequestMapping("/auth/endpoint")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EndpointController {

    EndpointService endpointService;

    @GetMapping("/all")
    public CustomApiResponse<List<EndpointResponse>> getAllEndpoints() {
        var response = endpointService.getAllEndpoints();
        return CustomApiResponse.<List<EndpointResponse>>builder()
                .data(response)
                .message("Successfully retrieved all endpoints")
                .build();
    }

    @GetMapping("/public")
    public CustomApiResponse<List<EndpointResponse>> getPublicEndpoints() {
        var response = endpointService.getPublicEndpoints();
        return CustomApiResponse.<List<EndpointResponse>>builder()
                .data(response)
                .message("Successfully retrieved all public endpoints")
                .build();
    }

    @GetMapping
    public CustomApiResponse<Page<EndpointEditableResponse>> view() {

        var response = endpointService.getEditableEndpoints(new EndpointFilter());
        return CustomApiResponse.<Page<EndpointEditableResponse>>builder()
                .data(response)
                .message("Successfully retrieved endpoints")
                .build();
    }

    @PostMapping
    public CustomApiResponse<Page<EndpointEditableResponse>> view(
            @RequestBody EndpointFilter filter
    ) {
        var response = endpointService.getEditableEndpoints(filter);
        return CustomApiResponse.<Page<EndpointEditableResponse>>builder()
                .data(response)
                .message("Successfully retrieved endpoints")
                .build();
    }

    @GetMapping("/{id}")
    public CustomApiResponse<EndpointDetailResponse> getEndpointById(@PathVariable String id) {
        var response = endpointService.getEndpointById(id);
        return CustomApiResponse.<EndpointDetailResponse>builder()
                .data(response)
                .message("Successfully retrieved endpoint by ID")
                .build();
    }

    @PatchMapping("/{id}")
    public CustomApiResponse<Void> updateEndpoint(
            @PathVariable String id,
            @RequestBody EndpointUpdateRequest endpointUpdateRequest
    ) {
        endpointService.updateEndpoint(id, endpointUpdateRequest);
        return CustomApiResponse.<Void>builder()
                .message("Successfully updated endpoint")
                .build();
    }





}
