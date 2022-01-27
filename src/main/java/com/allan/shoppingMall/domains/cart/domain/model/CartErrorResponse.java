package com.allan.shoppingMall.domains.cart.domain.model;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.domains.order.domain.model.OrderErrorResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * server order api response
 * order rest api 응답 클래스입니다.
 */
@Getter
@Setter
public class CartErrorResponse {
    private String errorCode; // server error code(ErrorCode class 참조).
    private int status; // server response status(ErrorCode class 참조).
    private String errMsg; // 에러 메시지.

    public CartErrorResponse(String errorCode, int status, String errMsg) {
        this.errorCode = errorCode;
        this.status = status;
        this.errMsg = errMsg;
    }

    /**
     * 에러코드로 cartErrorResponse 를 생성하는 메소드.
     * @param errorCode 에러코드
     * @return CartErrorResponse
     */
    public static CartErrorResponse of(final ErrorCode errorCode){
        return new CartErrorResponse(errorCode.getCode(), errorCode.getStatus(), errorCode.getMessage());
    }

    /**
     * @param errMsg Exception.getMessage()
     * @param errorCode ErrorCode
     * @return OrderErrorResponse
     */
    public static CartErrorResponse of(final String errMsg, final ErrorCode errorCode){
        return new CartErrorResponse(errorCode.getCode(), errorCode.getStatus(), errMsg);
    }
}
