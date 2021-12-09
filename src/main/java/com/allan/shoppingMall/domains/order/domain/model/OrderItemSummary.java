package com.allan.shoppingMall.domains.order.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  주문하는 상품의 요약정보.
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderItemSummary {
    private Long itemId;
    private String itemName;
    private SizeLabel size;
    private Long orderQuantity;
    private Long price;
    private Long previewImg;

    @Override
    public String toString() {
        return "OrderItemSummary [name=" + this.itemName + ", size=" + this.size.getDesc() + ", quantity=" + this.getOrderQuantity() + ", price=" + this.getPrice() + ", previewImg=" + this.previewImg + "]";
    }

    @Builder
    public OrderItemSummary(Long itemId, String itemName, SizeLabel size, Long orderQunatity, Long price, Long previewImg) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.size = size;
        this.orderQuantity = orderQunatity;
        this.price = price;
        this.previewImg = previewImg;
    }
}
