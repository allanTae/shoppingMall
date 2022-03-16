package com.allan.shoppingMall.domains.cart.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.*;

/**
 * 사용자로부터 전달 받은 장바구니 상품 정보 오브젝트.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartLineRequest {

    private Long itemId;
    private Long cartQuantity;
    private SizeLabel size;
    private Long categoryId;

    @Override
    public String toString() {
        return "CartItemSummary{" +
                "itemId=" + itemId +
                ", categoryId=" + categoryId +
                ", cartQuantity=" + cartQuantity +
                ", size=" + size +
                '}';
    }

}
