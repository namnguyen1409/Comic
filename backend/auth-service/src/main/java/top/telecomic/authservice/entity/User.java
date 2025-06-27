package top.telecomic.authservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import top.telecomic.authservice.entity.base.BaseEntity;

import java.util.Set;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@FieldNameConstants
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true)
    String username;

    @Column
    String email;

    @Column
    String phoneNumber;

    @Column(nullable = false)
    Boolean enabled = Boolean.FALSE;

    @Column(nullable = false)
    Boolean accountNonLocked = Boolean.TRUE;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_code", referencedColumnName = "code")
    )
    Set<Role> roles;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_revoked_permissions",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_code", referencedColumnName = "code")
    )
    Set<Permission> revokedPermissions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<AuthenticationMethod> authenticationMethods;

    @Column
    Boolean is2faEnabled = Boolean.FALSE;

    @Column
    String twoFactorSecret;

    @ElementCollection
    @CollectionTable(name = "user_2fa_recovery_codes", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "recovery_code")
    Set<String> twoFactorRecoveryCodes;


}
