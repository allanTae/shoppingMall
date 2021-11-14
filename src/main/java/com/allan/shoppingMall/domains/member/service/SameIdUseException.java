package com.allan.shoppingMall.domains.member.service;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

public class SameIdUseException extends BusinessException {

    public SameIdUseException(String message, ErrorCode errorCode){
        super(message,errorCode);
    }
}
