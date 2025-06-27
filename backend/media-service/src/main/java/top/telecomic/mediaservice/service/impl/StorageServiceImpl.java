package top.telecomic.mediaservice.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.telecomic.mediaservice.dto.request.MediaFileRequest;
import top.telecomic.mediaservice.enums.FileType;
import top.telecomic.mediaservice.exception.ErrorCode;
import top.telecomic.mediaservice.exception.GlobalException;
import top.telecomic.mediaservice.service.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StorageServiceImpl implements StorageService {

    Tika tika = new Tika();

    @NonFinal
    @Value("${media.storage.directory}")
    Path storageDirectory;

    @NonFinal
    @Value("${media.access.prefix}")
    String mediaAccessPrefix;

    @PostConstruct
    public void init() throws IOException {
        log.info("Initializing storage directory: {}", storageDirectory);
        Files.createDirectories(storageDirectory.toAbsolutePath().normalize());
    }


    @Override
    public MediaFileRequest store(MultipartFile file, FileType fileType, String directory, long maxSize, String... allowedExtensions) {
        try {
            validateFilePresence(file);
            validateFileSize(file, maxSize);
            String originalFilename = getOriginalFilename(file);
            validateFilename(originalFilename);
            String fileExtension = getFileExtension(originalFilename);
            validateExtension(fileExtension, allowedExtensions);
            validateImageMimeType(file);
            String fileName = UUID.randomUUID() + "." + fileExtension;
            Path targetDir = createDirectoryIfNotExist(directory);
            Path targetFile = targetDir.resolve(fileName);
            file.transferTo(targetFile);
            return new MediaFileRequest(
                    originalFilename,
                    file.getContentType(),
                    fileType,
                    file.getSize(),
                    String.join("/", mediaAccessPrefix, directory, fileName)
            );
        } catch (IOException e) {
            log.error("Failed to store file: {}", file.getOriginalFilename(), e);
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to store file: " + file.getOriginalFilename());
        }
    }

    @Override
    public Resource get(String directory, String filename) {
        Path filePath = storageDirectory.resolve(directory).resolve(filename);
        if (!Files.exists(filePath)) {
            throw new GlobalException(ErrorCode.MEDIA_NOT_FOUND, "File not found: " + filename);
        }
        return new PathResource(filePath);
    }


    @Override
    public void delete(String filePath) {
        try {
            filePath = filePath.replace(mediaAccessPrefix, "");
            var directory = filePath.split("/")[0];
            var filename = filePath.substring(filePath.indexOf("/") + 1);
            Path path = storageDirectory.resolve(directory).resolve(filename);
            log.info("Deleting file: {}", path);
            if (Files.exists(path)) {
                Files.delete(path);
            } else {
                throw new GlobalException(ErrorCode.MEDIA_NOT_FOUND, "File not found: " + filePath);
            }
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filePath, e);
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to delete file: " + filePath);
        }
    }

    // ============================
    // HELPER METHODS
    // ============================

    private void validateFilePresence(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new GlobalException(ErrorCode.MEDIA_NOT_FOUND);
        }
    }

    private void validateFileSize(MultipartFile file, long maxSize) {
        if (file.getSize() > maxSize) {
            throw new GlobalException(ErrorCode.MEDIA_SIZE_EXCEEDED);
        }
    }

    private String getOriginalFilename(MultipartFile file) {
        String name = file.getOriginalFilename();
        if (name == null || name.isEmpty()) {
            throw new GlobalException(ErrorCode.BAD_REQUEST);
        }
        return name;
    }

    private void validateFilename(String filename) {
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "Invalid file name: " + filename);
        }
    }

    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1 || lastDot == filename.length() - 1) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "Missing file extension");
        }
        return filename.substring(lastDot + 1).toLowerCase();
    }

    private void validateExtension(String extension, String[] allowedExtensions) {
        Set<String> allowed = Set.of(allowedExtensions);
        if (!allowed.contains(extension)) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "Invalid file extension: " + extension);
        }
    }

    private void validateImageMimeType(MultipartFile file) throws IOException {
        String mimeType = tika.detect(file.getBytes());
        if (!mimeType.startsWith("image/")) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "File is not an image: " + mimeType);
        }
    }

    private Path createDirectoryIfNotExist(String directory) throws IOException {
        Path dirPath = storageDirectory.resolve(directory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        return dirPath;
    }
}
