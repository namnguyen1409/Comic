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
import top.telecomic.authservice.criteria.RoleCriteria;
import top.telecomic.authservice.dto.filter.role.RoleFilter;
import top.telecomic.authservice.dto.request.role.RoleCreateRequest;
import top.telecomic.authservice.dto.request.role.RoleUpdateRequest;
import top.telecomic.authservice.dto.response.role.RoleResponse;
import top.telecomic.authservice.dto.response.role.RoleResponseDetail;
import top.telecomic.authservice.exception.ErrorCode;
import top.telecomic.authservice.exception.GlobalException;
import top.telecomic.authservice.mapper.RoleMapper;
import top.telecomic.authservice.repository.RoleRepository;
import top.telecomic.authservice.service.PermissionServiceHelper;
import top.telecomic.authservice.service.RoleService;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {


    RoleRepository roleRepository;
    RoleMapper roleMapper;
    RoleCriteria roleCriteria;
    PermissionServiceHelper permissionServiceHelper;


    @Override
    @Cacheable(cacheNames = "roles", key = "'roles:all'")
    public List<RoleResponse> getAllRoles() {
        try {
            var roles = roleRepository.findRolesByDeletedIsFalse();
            return roles.stream().map(roleMapper::toRoleResponseDto).toList();
        } catch (Exception e) {
            log.info("Failed to get all roles");
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @CacheEvict(cacheNames = "roles", key = "'roles:all'")
    public void clearRoleCache() {
        log.info("Cleared role cache");
    }

    @Override
    public Page<RoleResponseDetail> getRolesByPage(RoleFilter filter) {
        try {
            var pageable = PageRequest.of(
                    filter.getPage(),
                    filter.getSize()
            );
            var spec = roleCriteria.buildSpecification(filter);
            return roleRepository.findAll(spec, pageable).map(roleMapper::toRoleResponseDetailDto);
        } catch (Exception e) {
            log.info("Failed to get roles by page");
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public RoleResponseDetail getRoleByCode(String code) {
        return roleRepository.findById(code)
                .map(roleMapper::toRoleResponseDetailDto)
                .orElseThrow(() -> {
                    log.info("Role with code {} not found", code);
                    return new GlobalException(ErrorCode.ROLE_NOT_FOUND);
                });
    }

    @Override
    public RoleResponseDetail createRole(RoleCreateRequest roleCreateRequest) {
        try {
            var role = roleMapper.toEntity(roleCreateRequest);
            if (roleCreateRequest.permissionCodes() != null) {
                var permissions = permissionServiceHelper.resolvePermissions(
                        roleCreateRequest.permissionCodes(),
                        "Auto-generated permission for endpoint: " + role.getName() + ": "
                );
                role.setPermissions(permissions);
            }
            role = roleRepository.save(role);
            return roleMapper.toRoleResponseDetailDto(role);
        } catch (Exception e) {
            log.info("Failed to create role");
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public RoleResponseDetail updateRole(String code, RoleUpdateRequest roleUpdateRequest) {
        var role = roleRepository.findById(code)
                .orElseThrow(() -> new GlobalException(ErrorCode.ROLE_NOT_FOUND, "Role not found with code: " + code));

        roleMapper.partialUpdate(roleUpdateRequest, role);

        if (roleUpdateRequest.permissionCodes() != null) {
            if (roleUpdateRequest.permissionCodes().isEmpty()) {
                role.setPermissions(new HashSet<>());
            }
            else {
                var permissions = permissionServiceHelper.resolvePermissions(
                        roleUpdateRequest.permissionCodes(),
                        "Auto-generated permission for endpoint: " + role.getName() + ": "
                );
                role.setPermissions(new HashSet<>(permissions));
            }
        }
        // if null -> no change permissions list

        role = roleRepository.save(role);
        return roleMapper.toRoleResponseDetailDto(role);
    }

    @Override
    public void deleteRole(String code) {
        var role = roleRepository.findById(code)
                .orElseThrow(() -> new GlobalException(ErrorCode.ROLE_NOT_FOUND, "Role not found with code: " + code));
        role.setDeleted(true);
        roleRepository.save(role);
    }
}
