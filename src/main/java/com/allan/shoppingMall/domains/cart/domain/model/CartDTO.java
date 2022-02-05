package com.allan.shoppingMall.domains.cart.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

/**
 * 장바구니 목록 정보 클래스입니다.
 * (벡단에서 프론트단으로 장바구니 정보를 전달 하기 위해 사용합니다.)
 */
@Getter
@Setter
@NoArgsConstructor
public class CartDTO {

    private Long cartId; // 장바구니 도메인 식별자.
    private String ckId; // 쿠키 아이디.
    private HashMap<Long, CartItemDTO> cartItems = new HashMap<Long, CartItemDTO>();

    @Override
    public String toString() {
        return "CartDTO{" +
                "cartId=" + cartId +
                ", cartItems=" + cartItems +
                '}';
    }

    public CartDTO(Long cartId) {
        this.cartId = cartId;
    }
}
