package com.allan.shoppingMall.domains.cart.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * cart api 결과를 나타내는 enum 클래스 입니다.
 */
@AllArgsConstructor
@Getter
public enum CartResult {
    ADD_CART_SUCCESS(true, "상품을 장바구니에 추가하였습니다."),
    ADD_CART_FAIL(false, "상품을 장바구니에 추가하는데 실패하였습니다."),
    MODIFY_CART_SUCCESS(true, "장바구니 상품 변경에 성공하였습니다."),
    MODIFY_CART_FAIL(false, "장바구니 상품 변경에 실패하였습니다.");

    private Boolean result;
    private String message;
}
