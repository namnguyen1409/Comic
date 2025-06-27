package top.telecomic.mediaservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomApiResponse<T> {
    @Builder.Default
    int code = 200;
    @Builder.Default
    String serviceName = "media-service";
    String message;
    T data;
}
