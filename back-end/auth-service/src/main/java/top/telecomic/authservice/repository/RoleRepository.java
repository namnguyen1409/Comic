package top.telecomic.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.telecomic.authservice.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> , JpaSpecificationExecutor<Role> {
  }