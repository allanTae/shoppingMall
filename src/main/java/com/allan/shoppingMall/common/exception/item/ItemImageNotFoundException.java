package com.allan.shoppingMall.common.exception.item;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class ItemImageNotFoundException extends BusinessException {

    public ItemImageNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ItemImageNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
