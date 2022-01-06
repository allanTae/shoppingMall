package com.allan.shoppingMall.common.exception.order;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class RefundFailException extends BusinessException {

    public RefundFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public RefundFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
