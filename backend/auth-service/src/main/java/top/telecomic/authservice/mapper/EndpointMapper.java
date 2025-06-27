package top.telecomic.authservice.mapper;

import org.mapstruct.*;
import top.telecomic.authservice.dto.cache.EndpointCache;
import top.telecomic.authservice.dto.message.EndpointMessage;
import top.telecomic.authservice.dto.request.endpoint.EndpointUpdateRequest;
import top.telecomic.authservice.dto.response.endpoint.EndpointDetailResponse;
import top.telecomic.authservice.dto.response.endpoint.EndpointEditableResponse;
import top.telecomic.authservice.dto.response.endpoint.EndpointResponse;
import top.telecomic.authservice.entity.Endpoint;
import top.telecomic.authservice.entity.Permission;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {PermissionMapper.class})
public interface EndpointMapper {

    // Mapping between EndpointMessage and Endpoint
    @Mapping(target = "isPublic", expression = "java(true)")
    Endpoint toEntity(EndpointMessage endpointMessage);

    EndpointMessage toMessageDto(Endpoint endpoint);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endpoint partialUpdate(EndpointMessage endpointMessage, @MappingTarget Endpoint endpoint);

    // Mapping between EndpointCache and Endpoint
    Endpoint toEntity(EndpointCache endpointCache);

    @Mapping(target = "permissionCodes", expression = "java(permissionsToPermissionCodes(endpoint.getPermissions()))")
    EndpointCache toCacheDto(Endpoint endpoint);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endpoint partialUpdate(EndpointCache endpointCache, @MappingTarget Endpoint endpoint);

    default Set<String> permissionsToPermissionCodes(Set<Permission> permissions) {
        if (permissions == null) {
            return Set.of();
        }
        return permissions.stream().map(Permission::getCode).collect(Collectors.toSet());
    }

    // Mapping between EndpointResponse and Endpoint
    Endpoint toEntity(EndpointResponse endpointResponse);

    EndpointResponse toResponseDto(Endpoint endpoint);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endpoint partialUpdate(EndpointResponse endpointResponse, @MappingTarget Endpoint endpoint);

    // Mapping between EndpointEditableResponse and Endpoint
    Endpoint toEntity(EndpointEditableResponse endpointEditableResponse);

    EndpointEditableResponse toEditableResponseDto(Endpoint endpoint);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endpoint partialUpdate(EndpointEditableResponse endpointEditableResponse, @MappingTarget Endpoint endpoint);

    // Mapping between EndpointUpdateRequest and Endpoint
    Endpoint toEntity(EndpointUpdateRequest endpointUpdateRequest);

    @Mapping(target = "permissionCodes", expression = "java(permissionsToPermissionCodes(endpoint.getPermissions()))")
    EndpointUpdateRequest toUpdateRequestDto(Endpoint endpoint);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endpoint partialUpdate(EndpointUpdateRequest endpointUpdateRequest, @MappingTarget Endpoint endpoint);

    // Mapping between EndpointDetailResponse and Endpoint
    Endpoint toEntity(EndpointDetailResponse endpointDetailResponse);

    EndpointDetailResponse toEndpointDetailResponseDto(Endpoint endpoint);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Endpoint partialUpdate(EndpointDetailResponse endpointDetailResponse, @MappingTarget Endpoint endpoint);
}