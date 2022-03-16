package com.allan.shoppingMall.domains.cart.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 장바구니 상품 정보 클래스입니다.
 * (벡단에서 프론트단으로 장바구니 정보를 전달 하기 위해 사용합니다.)
 */
@Slf4j
@Getter
@Setter
public class CartItemDTO {
    private Long itemId;
    private Long itemProfileImg; // 장바구니 상품 프로필 이미지.
    private String itemName; // 장바구니 상품 이름.
    private Long itemPrice; // 장바구니 상품 가격.
    private Long deliveryPrice; // 배송비.
    private List<RequiredOption> requiredOptions = new ArrayList<>(); // 장바구니 상품의 필수옵션.
    private Long categoryId;

    public CartItemDTO(Long itemId, Long itemProfileImg, String itemName, Long itemPrice, Long cartQuantity, SizeLabel size, Long categoryId) {
        this.itemId = itemId;
        this.itemProfileImg = itemProfileImg;
        this.itemName = itemName;
        setPrice(itemPrice);
        requiredOptions.add(new RequiredOption(cartQuantity, size));
        this.categoryId = categoryId;
    }

    private void setPrice(Long itemPrice){
        this.itemPrice = itemPrice;
        if(itemPrice > 50000l)
            this.deliveryPrice = 0l;
        else{
            this.deliveryPrice = 3000l;
        }
    }

    @Override
    public String toString() {
        return "CartItemDTO{" +
                "itemId=" + itemId +
                ", itemProfileImg=" + itemProfileImg +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", deliveryPrice=" + deliveryPrice +
                ", requiredOptions=" + requiredOptions +
                ", categoryId=" + categoryId +
                '}';
    }
}
