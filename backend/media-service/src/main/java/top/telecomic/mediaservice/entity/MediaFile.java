package top.telecomic.mediaservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import top.telecomic.mediaservice.enums.FileType;

import java.util.List;


@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "media_file")
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class MediaFile extends BaseDocument {
    String fileName;
    String mediaType;
    FileType fileType;
    Long fileSize;
    String fileUrl;
    @Indexed
    List<String> tags;
    List<TagConfidence> tagsWithConfidence;
    Boolean isSensitive;
    String nsfwRating;
    Float nsfwConfidence;
}