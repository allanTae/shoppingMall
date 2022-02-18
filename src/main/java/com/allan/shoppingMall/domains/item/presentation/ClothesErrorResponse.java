package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.domains.cart.presentation.CartErrorResponse;

/**
 * Clothes 도메인 rest api 에러 정보를 저장하는 클래스입니다.
 */
public class ClothesErrorResponse {
    private String errorCode; // server error code(ErrorCode class 참조).
    private int status; // server response status(ErrorCode class 참조).
    private String errMsg; // 에러 메시지.

    public ClothesErrorResponse(String errorCode, int status, String errMsg) {
        this.errorCode = errorCode;
        this.status = status;
        this.errMsg = errMsg;
    }

    /**
     * 에러코드로 cartErrorResponse 를 생성하는 메소드.
     * @param errorCode 에러코드
     * @return CartErrorResponse
     */
    public static ClothesErrorResponse of(final ErrorCode errorCode){
        return new ClothesErrorResponse(errorCode.getCode(), errorCode.getStatus(), errorCode.getMessage());
    }
}
