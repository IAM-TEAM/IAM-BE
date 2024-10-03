package kr.iam.domain.advertisement.error;

import kr.iam.global.error.code.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AdvertisementError implements ErrorCode {
    ADVERTISEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "광고를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}