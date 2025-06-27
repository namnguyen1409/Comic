package top.telecomic.authservice.dto.filter.permission;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import top.telecomic.authservice.dto.filter.BaseFilter;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class PermissionFilter extends BaseFilter {
    String code;
    String name;
    String description;
}
