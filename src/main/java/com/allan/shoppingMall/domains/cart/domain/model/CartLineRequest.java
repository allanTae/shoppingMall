package com.allan.shoppingMall.domains.cart.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.*;

/**
 * 장바구니 상품 정보 클래스입니다.
 * (프론트단에서 벡단으로 장바구니 상품 정보를 전달 할 때 사용합니다.)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartLineRequest {

    private Long itemId;
    private Long cartQuantity;
    private SizeLabel size;

    @Override
    public String toString() {
        return "CartItemSummary{" +
                "itemId=" + itemId +
                ", cartQuantity=" + cartQuantity +
                ", size=" + size +
                '}';
    }

}
