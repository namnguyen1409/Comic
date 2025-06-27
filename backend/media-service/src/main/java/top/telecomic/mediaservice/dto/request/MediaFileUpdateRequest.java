package top.telecomic.mediaservice.dto.request;

import top.telecomic.mediaservice.entity.TagConfidence;

import java.io.Serializable;
import java.util.List;

public record MediaFileUpdateRequest(
        String id,
        List<String> tags,
        List<TagConfidence> tagsWithConfidence,
        Boolean isSensitive,
        String nsfwRating,
        Float nsfwConfidence
) implements Serializable {
}
