package top.telecomic.mediaservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "user_storage_quota")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserStorageQuota extends BaseDocument {
    String userId;
    Long storageQuota;
    Long storageUsage;
}
