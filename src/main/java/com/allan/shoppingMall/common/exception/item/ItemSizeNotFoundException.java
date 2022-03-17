package com.allan.shoppingMall.common.exception.item;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class ItemSizeNotFoundException extends BusinessException {

    public ItemSizeNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ItemSizeNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
