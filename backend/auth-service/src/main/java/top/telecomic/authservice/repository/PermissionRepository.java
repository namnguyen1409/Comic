package top.telecomic.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.telecomic.authservice.entity.Permission;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, String>, JpaSpecificationExecutor<Permission> {
    List<Permission> findPermissionByDeletedIsFalse();
}