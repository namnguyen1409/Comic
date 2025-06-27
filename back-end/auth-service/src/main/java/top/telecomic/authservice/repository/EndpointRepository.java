package top.telecomic.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.telecomic.authservice.entity.Endpoint;

import java.util.UUID;

public interface EndpointRepository extends JpaRepository<Endpoint, UUID> {
  }