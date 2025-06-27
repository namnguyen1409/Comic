package top.telecomic.authservice.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.telecomic.authservice.entity.Endpoint;

import java.util.List;
import java.util.UUID;


public interface EndpointRepository extends JpaRepository<Endpoint, UUID>, JpaSpecificationExecutor<Endpoint> {

    @NotNull
    @EntityGraph(attributePaths = {Endpoint.Fields.permissions})
    List<Endpoint> findAll();

    @NotNull
    @EntityGraph(attributePaths = {Endpoint.Fields.permissions})
    Page<Endpoint> findAll(Specification<Endpoint> specification, @NotNull Pageable pageable);

    @NotNull
    @EntityGraph(attributePaths = {Endpoint.Fields.permissions})
    List<Endpoint> findByIsPublicIsTrue();

    @NotNull
    @EntityGraph(attributePaths = {Endpoint.Fields.permissions})
    List<Endpoint> findByServiceName(String serviceName);

}