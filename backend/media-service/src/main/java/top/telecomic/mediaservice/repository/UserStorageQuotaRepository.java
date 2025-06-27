package top.telecomic.mediaservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import top.telecomic.mediaservice.entity.UserStorageQuota;

public interface UserStorageQuotaRepository extends MongoRepository<UserStorageQuota, String> {
}
