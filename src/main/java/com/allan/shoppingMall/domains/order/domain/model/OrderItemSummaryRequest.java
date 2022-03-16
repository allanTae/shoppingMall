package com.allan.shoppingMall.domains.order.domain.model;

import com.allan.shoppingMall.domains.cart.domain.model.RequiredOption;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Objects;

/**
 * 상세보기창에서(clothesDetail.jsp) 주문 창으로(orderForm.jsp) 주문 할 상품들의 정보들을
 * 전달하기 위해 사용함.(OrderSummaryRequest 에 포함 된 정보.)
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class OrderItemSummaryRequest {
    private Long itemId;
    private String itemName;
    private List<RequiredOption> requiredOptions;
    private Long price;
    private Long previewImg;
    private Long categoryId;

    @Override
    public String toString() {
        return "OrderItemSummary [name=" + this.itemName +  ", price=" + this.getPrice() + ", previewImg=" + this.previewImg + "]";
    }

    @Builder
    public OrderItemSummaryRequest(Long itemId, String itemName, Long price, Long previewImg, List<RequiredOption> requiredOptionList, Long categoryId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.previewImg = previewImg;
        this.requiredOptions = requiredOptionList;
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.itemId);
    }

    /**
     * orderClothes 경우, 동일한 itemId 를 가지고 있더라도 사이즈별로 주문 할 수 있기 때문에 주문 상품리스트에 동일한 itemId 를 가진 상품이 추가 될 수 있다.
     * 그렇기에 구별값을 itemId + sizeLabel 을 통해 구분 하도록 변경하였다.
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        log.info("OrderItemSummaryRequest equals() call!!!");
        if(obj == null){
            log.error("OrderItemSummaryRequest equals()'s parameter is null");
            return false;
        }
        if(this == obj)
            return true;
        if(!(obj instanceof OrderItemSummaryRequest))
            return false;

        return this.itemId == ((OrderItemSummaryRequest) obj).getItemId();
    }
}
