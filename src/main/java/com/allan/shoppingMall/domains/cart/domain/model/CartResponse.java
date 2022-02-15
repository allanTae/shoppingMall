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
    private String apiResultMessage; // 결과 메시지.
    private Boolean apiResult; // api 결과 값. 사용자단에서 결과를 확인하기 위해 사용합니다.

    private CartErrorResponse cartErrorResponse;

    /**
     * @param cartResult api 결과정보 오브젝트
     * @return
     */
    public CartResponse(CartResult cartResult){
        this.apiResultMessage = cartResult.getMessage();
        this.apiResult = cartResult.getResult();
    }

    /**
     * @param cartResult api 결과정보 오브젝트
     * @param cartErrorResponse api 에러정보 오브젝트
     * @return
     */
    public CartResponse(CartResult cartResult, CartErrorResponse cartErrorResponse){
        this.apiResultMessage = cartResult.getMessage();
        this.apiResult = cartResult.getResult();
        this.cartErrorResponse = cartErrorResponse;
    }

}
