package top.telecomic.mediaservice.service;

import org.springframework.web.multipart.MultipartFile;
import top.telecomic.mediaservice.dto.request.MediaFileUpdateRequest;
import top.telecomic.mediaservice.dto.response.MediaFileResponse;

public interface MediaFileService {
    MediaFileResponse uploadAvatar(MultipartFile file);

    void updateMediaFile(MediaFileUpdateRequest mediaFileUpdateRequest);

    MediaFileResponse getMediaFileById(String id);

    void deleteMediaFile(String id);
}
