package kr.iam.global.error.exception;


import kr.iam.global.error.code.ErrorCode;
import kr.iam.global.error.code.GlobalErrorCode;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException() {
        super(GlobalErrorCode.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

