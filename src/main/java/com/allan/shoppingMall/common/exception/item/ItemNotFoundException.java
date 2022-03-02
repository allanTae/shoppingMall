package com.allan.shoppingMall.common.exception.item;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class ItemNotFoundException extends BusinessException {
    public ItemNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ItemNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
