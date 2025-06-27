package top.telecomic.authservice.exception;

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
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED),
    // Authentication Service Errors
    KEY_GENERATION_FAILED(1001, "Key Generation Failed", HttpStatus.INTERNAL_SERVER_ERROR),
    KEY_CONVERSION_FAILED(1002, "Key Conversion Failed", HttpStatus.INTERNAL_SERVER_ERROR),
    KEY_NOT_FOUND(1002, "Key not found", HttpStatus.NOT_FOUND),

    DEVICE_USER_AGENT_NOT_FOUND(1003, "User-Agent header not found", HttpStatus.BAD_REQUEST),
    DEVICE_SAVE_FAILED(1004, "Failed to save device information", HttpStatus.INTERNAL_SERVER_ERROR),
    DEVICE_NOT_FOUND(1005, "Device not found", HttpStatus.NOT_FOUND),

    USER_NOT_FOUND(1006, "User not found", HttpStatus.NOT_FOUND),
    AUTHENTICATION_METHOD_NOT_FOUND(1007, "Authentication method not found", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS(1008, "Invalid credentials", HttpStatus.UNAUTHORIZED),
    TOKEN_CREATION_FAILED(1009, "Token creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_JWT(1010, "Invalid JWT token", HttpStatus.UNAUTHORIZED),
    INVALID_LOGIN_SESSION_STATUS(1011, "Invalid login session status", HttpStatus.UNAUTHORIZED),

    ENDPOINT_NOT_FOUND(1012, "Endpoint not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1013, "Role not found", HttpStatus.NOT_FOUND),


    ;

    int code;
    String message;
    HttpStatus httpStatus;

}
