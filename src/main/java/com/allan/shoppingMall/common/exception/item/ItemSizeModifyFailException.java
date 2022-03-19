package com.allan.shoppingMall.common.exception.item;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class ItemSizeModifyFailException extends BusinessException {
    public ItemSizeModifyFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ItemSizeModifyFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
