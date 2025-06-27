package top.telecomic.authservice.service;

import top.telecomic.authservice.entity.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionServiceHelper {
    Set<Permission> resolvePermissions(Set<String> codes, String fallbackDescriptionPrefix);
}
