package com.allan.shoppingMall.common.exception;

public class MemberNotFoundException extends BusinessException{

    public MemberNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
