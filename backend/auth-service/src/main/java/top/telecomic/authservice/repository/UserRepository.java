package top.telecomic.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.telecomic.authservice.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}