package top.telecomic.mediaservice.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import top.telecomic.mediaservice.dto.request.MediaFileRequest;
import top.telecomic.mediaservice.dto.request.MediaFileUpdateRequest;
import top.telecomic.mediaservice.dto.response.MediaFileResponse;
import top.telecomic.mediaservice.enums.FileType;
import top.telecomic.mediaservice.exception.ErrorCode;
import top.telecomic.mediaservice.exception.GlobalException;
import top.telecomic.mediaservice.mapper.MediaFileMapper;
import top.telecomic.mediaservice.repository.MediaFileRepository;
import top.telecomic.mediaservice.service.MediaFileService;
import top.telecomic.mediaservice.service.StorageService;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MediaFileServiceImpl implements MediaFileService {

    private final StorageService storageService;
    private final MediaFileRepository mediaFileRepository;
    private final MediaFileMapper mediaFileMapper;
    private final KafkaTemplate<String, MediaFileResponse> kafkaTemplate;

    @Value("${media.avatar.directory}")
    String avatarDirectory;

    @Value("${media.avatar.max-size}")
    DataSize avatarMaxSize;

    @Override
    public MediaFileResponse uploadAvatar(MultipartFile file) {
        MediaFileRequest request = storageService.store(file, FileType.AVATAR, avatarDirectory, avatarMaxSize.toBytes(), "jpg", "jpeg", "png", "gif");
        log.info("Stored file: {}", request);
        try {
            var responseEntity = mediaFileRepository.save(mediaFileMapper.toEntity(request));
            kafkaTemplate.send("media.to.ai", mediaFileMapper.toDto(responseEntity));
            return mediaFileMapper.toDto(responseEntity);
        } catch (Exception e) {
            log.error("Failed to store file: {}", file.getOriginalFilename(), e);
            throw new GlobalException(ErrorCode.MEDIA_UPLOAD_FAILED);
        }
    }


    @Override
    public void updateMediaFile(@NotNull MediaFileUpdateRequest mediaFileUpdateRequest) {
        var entity = mediaFileRepository.findById(mediaFileUpdateRequest.id())
                .orElseThrow(() -> new GlobalException(ErrorCode.MEDIA_NOT_FOUND));
        mediaFileRepository.save(mediaFileMapper.partialUpdate(mediaFileUpdateRequest, entity));
    }

    @Override
    public MediaFileResponse getMediaFileById(String id) {
        var entity = mediaFileRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEDIA_NOT_FOUND));
        return mediaFileMapper.toDto(entity);
    }

    @Override
    public void deleteMediaFile(String id) {
        try {
            var entity = mediaFileRepository.findById(id)
                    .orElseThrow(() -> new GlobalException(ErrorCode.MEDIA_NOT_FOUND));
            entity.softDelete();
            mediaFileRepository.save(entity);
            storageService.delete(entity.getFileUrl());
            log.info("Deleted media file: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete media file: {}", id, e);
            throw new GlobalException(ErrorCode.MEDIA_DELETE_FAILED);
        }
    }

}
