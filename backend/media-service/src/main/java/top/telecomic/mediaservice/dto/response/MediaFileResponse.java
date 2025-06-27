package top.telecomic.mediaservice.dto.response;

import lombok.Builder;
import top.telecomic.mediaservice.entity.TagConfidence;
import top.telecomic.mediaservice.enums.FileType;

import java.io.Serializable;
import java.util.List;


@Builder
public record MediaFileResponse(
        String id,
        String fileName,
        String mediaType,
        FileType fileType,
        Long fileSize,
        String fileUrl,
        List<String> tags,
        List<TagConfidence> tagsWithConfidence,
        Boolean isSensitive,
        String nsfwRating,
        Double nsfwConfidence
) implements Serializable {
}
