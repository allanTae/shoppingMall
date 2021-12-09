package com.allan.shoppingMall.domains.order.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 주문 할 상품의 요약 정보.
 * 주문시, 서버로 전달 할 각 주문 상품에 대한 정보들.
 */

@Getter
@Setter
@NoArgsConstructor
public class OrderLineRequest {

    private Long itemId;
    private Long orderQuantity;

    // clothes 상품 주문 할 때 사용하는 사이즈 정보.
    private SizeLabel size;

    @Builder
    public OrderLineRequest(Long itemId, Long orderQuantity, SizeLabel size) {
        this.itemId = itemId;
        this.orderQuantity = orderQuantity;
        this.size = size;
    }

    @Override
    public String toString() {
        return "OrderLineRequest{" +
                "itemId=" + itemId +
                ", orderQuantity=" + orderQuantity +
                ", size=" + size +
                '}';
    }
}
