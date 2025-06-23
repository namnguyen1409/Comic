package top.telecomic.authservice.mapper;

import org.mapstruct.*;
import top.telecomic.authservice.dto.response.PublicKeyResponse;
import top.telecomic.authservice.entity.Key;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface KeyMapper {

    Key toEntity(PublicKeyResponse publicKeyResponse);

    PublicKeyResponse toDto(Key key);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Key partialUpdate(PublicKeyResponse publicKeyResponse, @MappingTarget Key key);

}
