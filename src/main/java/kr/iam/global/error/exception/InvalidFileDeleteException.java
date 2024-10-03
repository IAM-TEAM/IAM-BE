package kr.iam.global.error.exception;


import kr.iam.global.error.code.ErrorCode;
import kr.iam.global.error.code.GlobalErrorCode;

public class InvalidFileDeleteException extends BusinessException {
    public InvalidFileDeleteException() {
        super(GlobalErrorCode.INVALID_FILE_DELETE);
    }

    public InvalidFileDeleteException(ErrorCode errorCode) {
        super(errorCode);
    }
}

