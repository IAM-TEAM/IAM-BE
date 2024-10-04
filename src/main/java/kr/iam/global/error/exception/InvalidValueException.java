package kr.iam.global.error.exception;

import kr.iam.global.error.code.ErrorCode;
import kr.iam.global.error.code.GlobalErrorCode;

public class InvalidValueException extends BusinessException {
    public InvalidValueException() {
        super(GlobalErrorCode.BAD_REQUEST);
    }

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
