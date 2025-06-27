package top.telecomic.mediaservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import top.telecomic.mediaservice.dto.response.CustomApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<CustomApiResponse<Void>> handleRuntimeException(
            Exception exception
    ) {
        var errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
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

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    ResponseEntity<CustomApiResponse<Void>> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException exception
    ) {
        var errorCode = ErrorCode.MEDIA_SIZE_EXCEEDED;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        CustomApiResponse.<Void>builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build()
                );
    }

}
