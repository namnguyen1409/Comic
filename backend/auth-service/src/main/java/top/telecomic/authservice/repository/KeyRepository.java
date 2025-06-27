package top.telecomic.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.telecomic.authservice.entity.Key;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KeyRepository extends JpaRepository<Key, UUID> {
    List<Key> findAllByIsActiveIsTrue();

    Optional<Key> findFirstByIsActiveIsTrueOrderByCreatedAtDesc();
}