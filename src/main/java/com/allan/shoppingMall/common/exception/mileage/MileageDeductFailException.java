package com.allan.shoppingMall.common.exception.mileage;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class MileageDeductFailException extends BusinessException {
    public MileageDeductFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public MileageDeductFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
