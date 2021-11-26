package com.allan.shoppingMall.common.exception;

public class ItemImageNotFoundException extends BusinessException{

    public ItemImageNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ItemImageNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
