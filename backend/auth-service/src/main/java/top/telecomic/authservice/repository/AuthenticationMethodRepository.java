package top.telecomic.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.telecomic.authservice.entity.AuthenticationMethod;
import top.telecomic.authservice.enums.AuthProvider;

import java.util.Optional;
import java.util.UUID;

public interface AuthenticationMethodRepository extends JpaRepository<AuthenticationMethod, UUID> {
    Optional<AuthenticationMethod> findByUserUsernameAndProvider(String userUsername, AuthProvider provider);
}