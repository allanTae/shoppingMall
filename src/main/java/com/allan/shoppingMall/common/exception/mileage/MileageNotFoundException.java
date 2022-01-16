package com.allan.shoppingMall.common.exception.mileage;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class MileageNotFoundException extends BusinessException {

    public MileageNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public MileageNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
