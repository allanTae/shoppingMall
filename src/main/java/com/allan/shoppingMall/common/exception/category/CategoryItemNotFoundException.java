package com.allan.shoppingMall.common.exception.category;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class CategoryItemNotFoundException extends BusinessException {

    public CategoryItemNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CategoryItemNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
