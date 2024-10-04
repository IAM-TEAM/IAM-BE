package kr.iam.global.error.exception;

import kr.iam.global.error.code.ErrorCode;
import kr.iam.global.error.code.GlobalErrorCode;

public class InvalidFileUploadException extends BusinessException {
    public InvalidFileUploadException() {
        super(GlobalErrorCode.INVALID_FILE_UPLOAD);
    }

    public InvalidFileUploadException(ErrorCode errorCode) {
        super(errorCode);
    }
}

