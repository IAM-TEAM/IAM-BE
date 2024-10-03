package kr.iam.global.error.exception;


import kr.iam.global.error.code.ErrorCode;
import kr.iam.global.error.code.GlobalErrorCode;

public class ConflictException extends BusinessException {
    public ConflictException() {
        super(GlobalErrorCode.CONFLICT);
    }

    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}


