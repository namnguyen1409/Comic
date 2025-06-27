package top.telecomic.authservice.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.telecomic.authservice.entity.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

    @NotNull
    @EntityGraph(attributePaths = {Role.Fields.permissions})
    List<Role> findRolesByDeletedIsFalse();

    @NotNull
    @EntityGraph(attributePaths = {Role.Fields.permissions})
    Page<Role> findAll(Specification<Role> specification, @NotNull Pageable pageable);
}