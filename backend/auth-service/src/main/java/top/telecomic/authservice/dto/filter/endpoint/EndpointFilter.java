package top.telecomic.authservice.dto.filter.endpoint;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import top.telecomic.authservice.dto.filter.BaseFilter;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class EndpointFilter extends BaseFilter implements Serializable {
    String path;
    String method;
    String description;
    Boolean isPublic;
}
