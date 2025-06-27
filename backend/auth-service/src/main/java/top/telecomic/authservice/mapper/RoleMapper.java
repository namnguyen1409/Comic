package top.telecomic.authservice.mapper;

import org.mapstruct.*;
import top.telecomic.authservice.dto.request.role.RoleCreateRequest;
import top.telecomic.authservice.dto.request.role.RoleUpdateRequest;
import top.telecomic.authservice.dto.response.role.RoleResponse;
import top.telecomic.authservice.dto.response.role.RoleResponseDetail;
import top.telecomic.authservice.entity.Permission;
import top.telecomic.authservice.entity.Role;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {PermissionMapper.class})
public interface RoleMapper {
    // Convert between RoleResponse and Role
    Role toEntity(RoleResponse roleResponse);

    RoleResponse toRoleResponseDto(Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Role partialUpdate(RoleResponse roleResponse, @MappingTarget Role role);

    // Convert between RoleResponseDetail and Role
    Role toEntity(RoleResponseDetail roleResponseDetail);

    RoleResponseDetail toRoleResponseDetailDto(Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Role partialUpdate(RoleResponseDetail roleResponseDetail, @MappingTarget Role role);

    Role toEntity(RoleCreateRequest roleCreateRequest);

    @Mapping(target = "permissionCodes", expression = "java(permissionsToPermissionCodes(role.getPermissions()))")
    RoleCreateRequest toRoleCreateRequestDto(Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Role partialUpdate(RoleCreateRequest roleCreateRequest, @MappingTarget Role role);

    default Set<String> permissionsToPermissionCodes(Set<Permission> permissions) {
        return permissions.stream().map(Permission::getCode).collect(Collectors.toSet());
    }

    Role toEntity(RoleUpdateRequest roleUpdateRequest);

    @Mapping(target = "permissionCodes", expression = "java(permissionsToPermissionCodes(role.getPermissions()))")
    RoleUpdateRequest toRoleUpdateRequestDto(Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Role partialUpdate(RoleUpdateRequest roleUpdateRequest, @MappingTarget Role role);
}