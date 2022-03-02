package com.allan.shoppingMall.common.exception.item;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class AccessorySaveFailException extends BusinessException {

    public AccessorySaveFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public AccessorySaveFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
