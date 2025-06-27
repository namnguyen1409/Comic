package top.telecomic.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.telecomic.authservice.entity.Device;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    boolean existsByDeviceFingerprint(String deviceFingerprint);

    Optional<Device> findByDeviceFingerprint(String deviceFingerprint);
}