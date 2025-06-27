package top.telecomic.authservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import top.telecomic.authservice.dto.response.CustomApiResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<CustomApiResponse<Void>> handleRuntimeException(
            Exception exception
    ) {
        var errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Unexpected error occurred", exception);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        CustomApiResponse.<Void>builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build()
                );
    }


    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<CustomApiResponse<Void>> handleNoResourceFoundException(
            NoResourceFoundException exception
    ) {
        var errorCode = ErrorCode.NOT_FOUND;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        CustomApiResponse.<Void>builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    ResponseEntity<CustomApiResponse<Void>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception
    ) {
        var errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        CustomApiResponse.<Void>builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = GlobalException.class)
    ResponseEntity<CustomApiResponse<Void>> handleGlobalException(
            GlobalException exception
    ) {
        var errorCode = exception.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        CustomApiResponse.<Void>builder()
                                .code(errorCode.getCode())
                                .message(exception.getMessage())
                                .build()
                );
    }


}
