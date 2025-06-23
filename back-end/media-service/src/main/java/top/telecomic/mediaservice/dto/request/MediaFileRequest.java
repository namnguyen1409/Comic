package top.telecomic.mediaservice.dto.request;


import top.telecomic.mediaservice.enums.FileType;

import java.io.Serializable;

public record MediaFileRequest(
        String fileName,
        String mediaType,
        FileType fileType,
        Long fileSize,
        String fileUrl
) implements Serializable {
}
