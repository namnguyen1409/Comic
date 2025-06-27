package top.telecomic.authservice.mapper;

import org.mapstruct.*;
import top.telecomic.authservice.dto.message.EndpointMessage;
import top.telecomic.authservice.entity.Endpoint;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)public interface EndpointMapper {
    Endpoint toEntity(EndpointMessage endpointMessage);

    EndpointMessage toDto(Endpoint endpoint);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)Endpoint partialUpdate(EndpointMessage endpointMessage, @MappingTarget Endpoint endpoint);
}