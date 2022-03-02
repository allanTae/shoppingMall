package com.allan.shoppingMall.common.exception.item;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class ClothesSaveFailException extends BusinessException {
    public ClothesSaveFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ClothesSaveFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
