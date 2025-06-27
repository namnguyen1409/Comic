package top.telecomic.authservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import top.telecomic.authservice.entity.base.NoIdBaseEntity;
import top.telecomic.authservice.enums.CryptoAlgorithm;

import java.util.UUID;


@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@FieldNameConstants
@Table(name = "keys")
public class Key extends NoIdBaseEntity {

    @Id
    @Column(nullable = false, unique = true)
    UUID keyId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    CryptoAlgorithm algorithm;

    @Column(nullable = false, columnDefinition = "TEXT")
    String privateKey;

    @Column(nullable = false, columnDefinition = "TEXT")
    String publicKey;

    @Column(nullable = false)
    Boolean isActive = true;
}
