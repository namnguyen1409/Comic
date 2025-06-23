package top.telecomic.mediaservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public abstract class BaseDocument {
    @Id
    String id;

    @CreatedBy
    String createdBy;

    @CreatedDate
    Instant createdAt;

    @LastModifiedBy
    String updatedBy;

    @LastModifiedDate
    Instant updatedAt;

    Boolean isDeleted = false;

    public void softDelete() {
        this.isDeleted = true;
    }
}
