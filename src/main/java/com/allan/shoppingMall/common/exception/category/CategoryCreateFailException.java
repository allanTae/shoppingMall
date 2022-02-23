package com.allan.shoppingMall.common.exception.category;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;

/**
 * 카테고리 도메인 생성 실패 예외 클래스 입니다.
 */
public class CategoryCreateFailException extends BusinessException {
    public CategoryCreateFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CategoryCreateFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
