package top.telecomic.authservice.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import top.telecomic.authservice.entity.Permission;
import top.telecomic.authservice.repository.PermissionRepository;
import top.telecomic.authservice.service.PermissionServiceHelper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionServiceHelperImpl implements PermissionServiceHelper {


    PermissionRepository permissionRepository;

    @Override
    public Set<Permission> resolvePermissions(Set<String> codes, String fallbackDescriptionPrefix) {
        if (codes == null || codes.isEmpty()) return Collections.emptySet();

        var existing = permissionRepository.findAllById(codes);
        var existingMap = existing.stream()
                .collect(Collectors.toMap(Permission::getCode, p -> p));

        List<Permission> finalPermissions = new ArrayList<>();

        for (String code : codes) {
            if (existingMap.containsKey(code)) {
                finalPermissions.add(existingMap.get(code));
            } else {
                var newPermission = new Permission(
                        code,
                        code,
                        fallbackDescriptionPrefix + code
                );
                finalPermissions.add(permissionRepository.save(newPermission));
            }
        }

        return new HashSet<>(finalPermissions);
    }
}
