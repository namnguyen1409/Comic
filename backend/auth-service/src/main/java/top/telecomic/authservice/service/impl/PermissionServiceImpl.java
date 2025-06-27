package top.telecomic.authservice.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.telecomic.authservice.dto.response.permission.PermissionResponse;
import top.telecomic.authservice.mapper.PermissionMapper;
import top.telecomic.authservice.repository.PermissionRepository;
import top.telecomic.authservice.service.PermissionService;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {


    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    @Cacheable(cacheNames = "permissions", key = "'permissions:all'")
    public List<PermissionResponse> getAllPermissions() {
        var permissions = permissionRepository.findPermissionByDeletedIsFalse();
        return permissions.stream().map(permissionMapper::toDto).toList();
    }

}
