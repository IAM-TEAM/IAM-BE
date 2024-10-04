package kr.iam.global.error.exception;


import kr.iam.global.error.code.ErrorCode;
import kr.iam.global.error.code.GlobalErrorCode;

public class InternalServerException extends BusinessException {
    public InternalServerException() {
        super(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}

