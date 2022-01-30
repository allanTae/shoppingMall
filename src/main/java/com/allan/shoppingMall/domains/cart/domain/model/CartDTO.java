package com.allan.shoppingMall.domains.cart.domain.model;

import com.allan.shoppingMall.domains.cart.domain.Cart;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

/**
 * 장바구니 목록 정보 클래스입니다.
 * (벡단에서 프론트단으로 장바구니 정보를 전달 하기 위해 사용합니다.)
 */
@Getter
@Setter
public class CartDTO {

    private Long cartId; // 장바구니 도메인 식별자.
    private List<CartItemDTO> cartItems;
//    private HashMap<String, CartItemDTO> cartItemMap;

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
