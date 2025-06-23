package top.telecomic.mediaservice.mapper;

import org.mapstruct.*;
import top.telecomic.mediaservice.dto.request.MediaFileRequest;
import top.telecomic.mediaservice.dto.request.MediaFileUpdateRequest;
import top.telecomic.mediaservice.dto.response.MediaFileResponse;
import top.telecomic.mediaservice.entity.MediaFile;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MediaFileMapper {

    MediaFile toEntity(MediaFileRequest mediaFileRequest);

    MediaFileResponse toDto(MediaFile mediaFile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MediaFile partialUpdate(MediaFileUpdateRequest mediaFileUpdateRequest, @MappingTarget MediaFile mediaFile);

}
