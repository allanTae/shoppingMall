package com.allan.shoppingMall.common.exception.cart;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class CartModifyFailException extends BusinessException {

    public CartModifyFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CartModifyFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
