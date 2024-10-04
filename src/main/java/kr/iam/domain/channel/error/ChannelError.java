package kr.iam.domain.channel.error;

import kr.iam.global.error.code.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ChannelError implements ErrorCode {
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
