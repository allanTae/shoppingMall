package com.allan.shoppingMall.common.exception.order;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class RefundOrderFailException extends RefundFailException {

    public RefundOrderFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public RefundOrderFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
