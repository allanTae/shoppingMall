package com.allan.shoppingMall.domains.order.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *  주문하는 상품의 요약정보.
 */
@Getter
@Setter
public class OrderItemSummary {
    private String itemName;
    private SizeLabel size;
    private Long quantity;
    private Long price;
    private Long previewImg;

    @Override
    public String toString() {
        return "OrderItemSummary [name=" + this.itemName + ", size=" + this.size.getDesc() + ", quantity=" + this.getQuantity() + ", price=" + this.getPrice() + ", previewImg=" + this.previewImg + "]";
    }

    @Builder
    public OrderItemSummary(String itemName, SizeLabel size, Long quantity, Long price, Long previewImg) {
        this.itemName = itemName;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.previewImg = previewImg;
    }
}
