package com.allan.shoppingMall.domains.cart.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * 사용자로부터 전달 받은 장바구니 정보 오브젝트.
 */
@Getter
@Setter
@NoArgsConstructor
public class CartRequest {

    // 쿠키 value 값(백엑드단의 컨트롤러단에서 cookieId 값을 서비스단으로 전달하기 위한 필드).
    private String cartCkId;

    // 장바구니 상품 정보(프론트단에서 전달 한 상품 정보).
    private List<CartLineRequest> cartItems;

    @Override
    public String toString() {
        return "CartRequest{" +
                "cartCkId='" + cartCkId + '\'' +
                ", cartItems=" + cartItems +
                '}';
    }

    @Builder
    public CartRequest(String cartCkId, List<CartLineRequest> cartItems) {
        this.cartCkId = cartCkId;
        this.cartItems = cartItems;
    }


}
