package com.allan.shoppingMall.common.exception.category;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class CategoryNotFoundException extends BusinessException {

    public CategoryNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CategoryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
