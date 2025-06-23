package top.telecomic.authservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import top.telecomic.authservice.entity.base.NoUserAuditBaseEntity;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@FieldNameConstants
@Table(name = "devices", indexes = {
        @Index(name = "idx_device_fingerprint", columnList = "deviceFingerprint", unique = true)
})
public class Device extends NoUserAuditBaseEntity {

    @Column(nullable = false, unique = true)
    String deviceFingerprint;

    @Column
    String deviceName;

    @Column
    String operatingSystemClass;

    @Column
    String operatingSystemName;

    @Column
    String operatingSystemVersion;

    @Column
    String agentClass;

    @Column
    String agentNameVersion;

    @Column(columnDefinition = "TEXT")
    String userAgent;


}
