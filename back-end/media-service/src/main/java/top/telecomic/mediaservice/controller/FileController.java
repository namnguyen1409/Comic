package top.telecomic.mediaservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.telecomic.mediaservice.service.StorageService;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("${media.access.prefix}")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "File Service", description = "File Service API")
public class FileController {


    StorageService storageService;


    @GetMapping(value = "/{directory}/{fileName:.+}", produces = "application/octet-stream")
    public ResponseEntity<Resource> getFile(
            @PathVariable String directory,
            @PathVariable String fileName
    ) throws IOException {
        Resource resource = storageService.get(directory, fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(Files.probeContentType(resource.getFile().toPath())))
                .body(resource);
    }

}
