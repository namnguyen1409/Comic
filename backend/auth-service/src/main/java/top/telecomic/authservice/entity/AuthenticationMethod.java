package top.telecomic.authservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import top.telecomic.authservice.entity.base.NoUserAuditBaseEntity;
import top.telecomic.authservice.enums.AuthProvider;


@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@FieldNameConstants
@Table(name = "authentication_methods")
public class AuthenticationMethod extends NoUserAuditBaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    AuthProvider provider;

    @Column
    String providerUserId;

    @Column
    String passwordHash;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}
