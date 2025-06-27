package top.telecomic.authservice.service;

import top.telecomic.authservice.dto.response.permission.PermissionResponse;

import java.util.List;

public interface PermissionService {
    List<PermissionResponse> getAllPermissions();
}
