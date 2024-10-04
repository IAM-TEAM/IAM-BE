package kr.iam.global.error.exception;


import kr.iam.global.error.code.ErrorCode;
import kr.iam.global.error.code.GlobalErrorCode;

public class MethodNotAllowedException extends BusinessException {
    public MethodNotAllowedException() {
        super(GlobalErrorCode.METHOD_NOT_ALLOWED);
    }

    public MethodNotAllowedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
