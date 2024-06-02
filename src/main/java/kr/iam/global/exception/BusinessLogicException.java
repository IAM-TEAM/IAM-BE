package kr.iam.global.exception;

import kr.iam.global.exception.code.ExceptionCode;
import lombok.Getter;

@Getter
public class BusinessLogicException extends RuntimeException{

    private final ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
