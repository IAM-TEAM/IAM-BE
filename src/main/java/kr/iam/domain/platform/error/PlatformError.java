package kr.iam.domain.platform.error;

import kr.iam.global.error.code.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum PlatformError implements ErrorCode {
    PLATFORM_NOT_FOUND(HttpStatus.NOT_FOUND, "플랫폼을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
