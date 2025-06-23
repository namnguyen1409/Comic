package top.telecomic.mediaservice.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    // Common Errors
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(400, "Bad Request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "Forbidden", HttpStatus.FORBIDDEN),
    NOT_FOUND(404, "Not Found", HttpStatus.NOT_FOUND),
    CONFLICT(409, "Conflict", HttpStatus.CONFLICT),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity", HttpStatus.UNPROCESSABLE_ENTITY),
    SERVICE_UNAVAILABLE(503, "Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    GATEWAY_TIMEOUT(504, "Gateway Timeout", HttpStatus.GATEWAY_TIMEOUT),
    // Media Service Specific Errors
    MEDIA_NOT_FOUND(1001, "Media not found", HttpStatus.NOT_FOUND),
    MEDIA_SIZE_EXCEEDED(1002, "Media size exceeds limit", HttpStatus.BAD_REQUEST),
    MEDIA_UPLOAD_FAILED(1003, "Media upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    MEDIA_DELETE_FAILED(1004, "Media delete failed", HttpStatus.INTERNAL_SERVER_ERROR);
    int code;
    String message;
    HttpStatus httpStatus;

}
