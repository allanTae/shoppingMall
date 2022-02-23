package com.allan.shoppingMall.domains.category.presentation;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.domains.cart.presentation.CartErrorResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * server category api response
 * category rest api 응답 클래스입니다.
 */
@Getter
public class CategoryErrorResponse {
    private String errorCode; // server error code(ErrorCode class 참조).
    private int status; // server response status(ErrorCode class 참조).
    private String errMsg; // 에러 메시지.

    public CategoryErrorResponse(String errorCode, int status, String errMsg) {
        this.errorCode = errorCode;
        this.status = status;
        this.errMsg = errMsg;
    }

    /**
     * 에러코드로 cartErrorResponse 를 생성하는 메소드.
     * @param errorCode 에러코드
     * @return CategoryErrorResponse
     */
    public static CategoryErrorResponse of(final ErrorCode errorCode){
        return new CategoryErrorResponse(errorCode.getCode(), errorCode.getStatus(), errorCode.getMessage());
    }
}
