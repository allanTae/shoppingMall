package com.allan.shoppingMall.common.exception;

public class ItemNotFoundException extends BusinessException{
    public ItemNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ItemNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
