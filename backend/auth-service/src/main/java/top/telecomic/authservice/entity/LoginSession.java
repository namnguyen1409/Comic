package top.telecomic.authservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import top.telecomic.authservice.entity.base.NoUserAuditBaseEntity;
import top.telecomic.authservice.enums.AuthProvider;
import top.telecomic.authservice.enums.LoginSessionStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@FieldNameConstants
@Table(name = "login_sessions")
public class LoginSession extends NoUserAuditBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @Column
    UUID accessTokenId;

    @Column
    UUID refreshTokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    Device device;


    @Column
    String ipAddress;

    @Column
    String userAgent;

    @Column
    Instant expiredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    LoginSessionStatus status = LoginSessionStatus.FAILED;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    AuthProvider provider;


}
