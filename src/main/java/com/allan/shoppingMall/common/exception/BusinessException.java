package com.allan.shoppingMall.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private ErrorCode errorCode;

    // 외부 라이브러리 사용시, 외부 라이브러리 예외 메시지를 사용하여 message 설정.
    public BusinessException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    // 외부 라이브러리를 사용하지 않는 경우.
    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
