package com.allan.shoppingMall.domains.order.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 상세보기창에서(clothesDetail.jsp) 주문 창으로(orderForm.jsp) 주문 할 상품들의 정보들을
 * 전달하기 위해 사용함.(OrderSummaryRequest 에 포함 된 정보.)
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderItemSummaryRequest {
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
    public OrderItemSummaryRequest(Long itemId, String itemName, SizeLabel size, Long orderQunatity, Long price, Long previewImg) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.size = size;
        this.orderQuantity = orderQunatity;
        this.price = price;
        this.previewImg = previewImg;
    }
}
