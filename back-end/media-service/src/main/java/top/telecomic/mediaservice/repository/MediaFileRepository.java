package top.telecomic.mediaservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import top.telecomic.mediaservice.entity.MediaFile;

public interface MediaFileRepository extends MongoRepository<MediaFile, String> {
}
