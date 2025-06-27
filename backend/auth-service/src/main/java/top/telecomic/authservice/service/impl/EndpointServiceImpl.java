package top.telecomic.authservice.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.telecomic.authservice.config.AuditorContext;
import top.telecomic.authservice.criteria.EndpointCriteria;
import top.telecomic.authservice.dto.filter.endpoint.EndpointFilter;
import top.telecomic.authservice.dto.message.EndpointMessage;
import top.telecomic.authservice.dto.request.endpoint.EndpointUpdateRequest;
import top.telecomic.authservice.dto.response.endpoint.EndpointDetailResponse;
import top.telecomic.authservice.dto.response.endpoint.EndpointEditableResponse;
import top.telecomic.authservice.dto.response.endpoint.EndpointResponse;
import top.telecomic.authservice.entity.Permission;
import top.telecomic.authservice.exception.ErrorCode;
import top.telecomic.authservice.exception.GlobalException;
import top.telecomic.authservice.mapper.EndpointMapper;
import top.telecomic.authservice.repository.EndpointRepository;
import top.telecomic.authservice.repository.PermissionRepository;
import top.telecomic.authservice.service.EndpointService;
import top.telecomic.authservice.service.PermissionServiceHelper;
import top.telecomic.authservice.service.cache.EndpointCacheService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EndpointServiceImpl implements EndpointService {

    EndpointMapper endpointMapper;
    EndpointRepository endpointRepository;
    EndpointCacheService endpointCacheService;
    PermissionRepository permissionRepository;
    private final EndpointCriteria endpointCriteria;
    private final PermissionServiceHelper permissionServiceHelper;


    @Override
    @Transactional
    public void addEndpoints(List<EndpointMessage> newEndpoints) {
        if (CollectionUtils.isEmpty(newEndpoints)) {
            return;
        }
        var serviceName = newEndpoints.getFirst().serviceName();
        AuditorContext.set(serviceName + "-system");
        try {
            var existingEndpoints = endpointRepository.findByServiceName(serviceName);
            var existingDtos = existingEndpoints.stream()
                    .map(endpointMapper::toMessageDto)
                    .collect(Collectors.toSet());
            var newDtos = new HashSet<>(newEndpoints);
            var deprecatedEndpoints = existingEndpoints.stream()
                    .filter(oldEndpoint -> !newDtos.contains(endpointMapper.toMessageDto(oldEndpoint)))
                    .toList();
            if (!deprecatedEndpoints.isEmpty()) {
                endpointRepository.deleteAll(deprecatedEndpoints);
                log.info("Deleted deprecated endpoints: {}", deprecatedEndpoints.size());
            }
            var toSave = newEndpoints.stream()
                    .filter(newDto -> !existingDtos.contains(newDto))
                    .map(endpointMapper::toEntity)
                    .toList();
            if (!toSave.isEmpty()) {
                endpointRepository.saveAll(toSave);
                log.info("Saved new endpoints: {}", toSave.size());
            } else {
                log.info("No new endpoints found");
            }
            endpointCacheService.sync(serviceName);

        } catch (Exception e) {
            log.error("Failed to add endpoints for service {}: {}", serviceName, e.getMessage(), e);
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            AuditorContext.clear();
        }
    }

    @Cacheable(cacheNames = "endpoints", key = "'endpoints:all'")
    @Override
    public List<EndpointResponse> getAllEndpoints() {
        log.info("Fetching all endpoints from cache or database");
        try {
            var endpoints = endpointRepository.findAll();
            return endpoints.stream().map(endpointMapper::toResponseDto).toList();
        } catch (Exception e) {
            log.error("Failed to fetch all endpoints: {}", e.getMessage(), e);
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    @Cacheable(cacheNames = "endpoints", key = "'endpoints:public'")
    @Override
    public List<EndpointResponse> getPublicEndpoints() {

        log.info("Fetching public endpoints from cache or database");
        try {
            var endpoints = endpointRepository.findByIsPublicIsTrue();
            return endpoints.stream()
                    .map(endpointMapper::toResponseDto)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to fetch public endpoints: {}", e.getMessage(), e);
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    @CacheEvict(cacheNames = "endpoints", key = "'endpoints:all'")
    @Override
    public void invalidateCacheAllEndpoints() {
        log.info("Invalidated cache for all endpoints");
    }

    @Override
    public Page<EndpointEditableResponse> getEditableEndpoints(EndpointFilter endpointFilter) {
        var pageable = PageRequest.of(
                endpointFilter.getPage(),
                endpointFilter.getSize()
        );
        var spec = endpointCriteria.buildSpecification(endpointFilter);
        return endpointRepository.findAll(spec, pageable).map(endpointMapper::toEditableResponseDto);
    }

    @Override
    public EndpointDetailResponse getEndpointById(String id) {
        return endpointRepository.findById(UUID.fromString(id)).map(endpointMapper::toEndpointDetailResponseDto)
                .orElseThrow(
                        () -> new GlobalException(ErrorCode.ENDPOINT_NOT_FOUND, "Endpoint not found with ID: " + id)
                );
    }

    @Override
    public void updateEndpoint(String id, EndpointUpdateRequest endpointUpdateRequest) {
        var endpoint = endpointRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new GlobalException(ErrorCode.ENDPOINT_NOT_FOUND, "Endpoint not found with ID: " + id));
        endpointMapper.partialUpdate(endpointUpdateRequest, endpoint);
        if (endpointUpdateRequest.permissionCodes() != null) {
            var permissions = permissionServiceHelper.resolvePermissions(
                    endpointUpdateRequest.permissionCodes(),
                    "Auto-generated permission for endpoint: " + endpoint.getPath() + ": "
            );
            endpoint.setPermissions(permissions);
        }
        endpointRepository.save(endpoint);
    }

}
