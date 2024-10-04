package kr.iam.global.error.exception;


import kr.iam.global.error.code.ErrorCode;
import kr.iam.global.error.code.GlobalErrorCode;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException() {
        super(GlobalErrorCode.UNAUTHORIZED);
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}

