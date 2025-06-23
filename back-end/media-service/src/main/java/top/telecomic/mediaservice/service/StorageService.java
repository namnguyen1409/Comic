package top.telecomic.mediaservice.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import top.telecomic.mediaservice.dto.request.MediaFileRequest;
import top.telecomic.mediaservice.enums.FileType;


public interface StorageService {
    MediaFileRequest store(MultipartFile file, FileType fileType, String directory, long maxSize, String... allowedExtensions);

    Resource get(String directory, String filename);

    void delete(String filePath);
}
