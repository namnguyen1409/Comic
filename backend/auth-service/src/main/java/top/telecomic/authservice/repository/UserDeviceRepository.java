package top.telecomic.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.telecomic.authservice.entity.UserDevice;

import java.util.Optional;
import java.util.UUID;

public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {
    Optional<UserDevice> findByUserUsernameAndDeviceId(String userUsername, UUID deviceId);
}