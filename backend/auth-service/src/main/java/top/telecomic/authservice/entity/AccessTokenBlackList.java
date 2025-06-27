package top.telecomic.authservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import top.telecomic.authservice.entity.base.NoIdBaseEntity;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@FieldNameConstants
@Table(name = "access_token_blacklists")
public class AccessTokenBlackList extends NoIdBaseEntity {

    @Id
    UUID accessTokenId;

}
