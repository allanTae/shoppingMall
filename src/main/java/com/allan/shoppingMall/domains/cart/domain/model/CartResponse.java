package com.allan.shoppingMall.domains.cart.domain.model;
import lombok.Getter;
import lombok.Setter;

/**
 * server cart api response
 * cart rest api 응답 클래스입니다.
 */
@Getter
@Setter
public class CartResponse {
    private String cartResultMessage; // 결과 메시지.
    private Long cartResult; // api 결과 값. 사용자단에서 결과를 확인하기 위해 사용합니다.

    private CartErrorResponse cartErrorResponse;

    public CartResponse(String orderResult, Long cartResult) {
        this.cartResultMessage = orderResult;
        this.cartResult = cartResult;
    }

    public CartResponse(CartResult cartResult){
        this.cartResultMessage = cartResult.getMessage();
        this.cartResult = cartResult.getResult();
    }

    public CartResponse(CartResult cartResult, CartErrorResponse cartErrorResponse){
        this.cartResultMessage = cartResult.getMessage();
        this.cartResult = cartResult.getResult();
        this.cartErrorResponse = cartErrorResponse;
    }

    public CartResponse(String orderResult, Long cartResult, CartErrorResponse cartErrorResponse) {
        this.cartResultMessage = orderResult;
        this.cartResult = cartResult;
        this.cartErrorResponse = cartErrorResponse;
    }
}
