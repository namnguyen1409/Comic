package top.telecomic.mediaservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.telecomic.mediaservice.dto.response.CustomApiResponse;
import top.telecomic.mediaservice.dto.response.MediaFileResponse;
import top.telecomic.mediaservice.service.MediaFileService;

@RestController
@RequestMapping("/media")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Media Service", description = "Media Service API")
public class MediaFileController {

    MediaFileService mediaFileService;

    @PostMapping(value = "/upload/avatar", consumes = "multipart/form-data")
    public CustomApiResponse<MediaFileResponse> uploadAvatar(@RequestParam("file") MultipartFile file) {
        MediaFileResponse response = mediaFileService.uploadAvatar(file);
        return CustomApiResponse.<MediaFileResponse>builder()
                .data(response)
                .build();
    }

    @PostMapping(value = "/upload/thumbnail", consumes = "multipart/form-data")
    public CustomApiResponse<MediaFileResponse> uploadThumbnail(@RequestParam("file") MultipartFile file) {
        MediaFileResponse response = mediaFileService.uploadThumbnail(file);
        return CustomApiResponse.<MediaFileResponse>builder()
                .data(response)
                .build();
    }

    @PostMapping(value = "/upload/chapter", consumes = "multipart/form-data")
    public CustomApiResponse<MediaFileResponse> uploadCover(@RequestParam("file") MultipartFile file) {
        MediaFileResponse response = mediaFileService.uploadChapter(file);
        return CustomApiResponse.<MediaFileResponse>builder()
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public CustomApiResponse<MediaFileResponse> getMediaFileById(@PathVariable String id) {
        MediaFileResponse response = mediaFileService.getMediaFileById(id);
        return CustomApiResponse.<MediaFileResponse>builder()
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public CustomApiResponse<Void> deleteMediaFile(@PathVariable String id) {
        mediaFileService.deleteMediaFile(id);
        return CustomApiResponse.<Void>builder()
                .data(null)
                .build();
    }


}
