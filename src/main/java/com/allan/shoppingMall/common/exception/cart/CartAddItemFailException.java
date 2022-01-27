package com.allan.shoppingMall.common.exception.cart;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class CartAddItemFailException extends BusinessException {
    public CartAddItemFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CartAddItemFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
