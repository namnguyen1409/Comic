package top.telecomic.authservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import top.telecomic.authservice.entity.base.NoIdBaseEntity;
import top.telecomic.authservice.enums.AuthProvider;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@FieldNameConstants
@Table(name = "auth_provider_configs")
public class AuthProviderConfig extends NoIdBaseEntity {

    @Id
    @Enumerated(EnumType.STRING)
    AuthProvider provider;

    @Column(nullable = false)
    String clientId;

    @Column(nullable = false)
    String clientSecret;

    @Column(nullable = false)
    String verifyEndpoint;

}
