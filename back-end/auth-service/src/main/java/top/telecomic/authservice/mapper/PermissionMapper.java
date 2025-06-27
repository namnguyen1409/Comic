package top.telecomic.authservice.mapper;

import org.mapstruct.*;
import top.telecomic.authservice.dto.response.permission.PermissionResponse;
import top.telecomic.authservice.entity.Permission;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)public interface PermissionMapper {
    Permission toEntity(PermissionResponse permissionResponse);

    PermissionResponse toDto(Permission permission);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)Permission partialUpdate(PermissionResponse permissionResponse, @MappingTarget Permission permission);
}