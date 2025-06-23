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
@Table(name = "endpoints")
public class Endpoint extends BaseEntity {

    @Column(nullable = false, updatable = false)
    String serviceName;

    @Column(nullable = false, updatable = false)
    String path;

    @Column(nullable = false, updatable = false)
    String method;

    @Column
    String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "endpoint_permissions",
            joinColumns = @JoinColumn(name = "endpoint_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_code", referencedColumnName = "code")
    )
    Set<Permission> permissions;


    Boolean isPublic = Boolean.TRUE;

}
