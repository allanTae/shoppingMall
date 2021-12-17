package com.allan.shoppingMall.domains.order.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 주문 상품에 대한 상세 정보를 가지고 있는 객체.
 */
@Getter
@Setter
public class OrderItemDTO {

    private Long profileImg; // 주문 상품 프로필 이미지.
    private Long price; // 주문 상품 가격.
    private Long orderQuantity; // 주문량.
    private String name; // 주문 상품 이름.
    private String color; // 주문 상품 컬러.
    private SizeLabel size; // 주문 상품 사이즈.

    private Long discountPrice; // 주문 상품 할인 가격.
    private String discountName; // 적용 된 할인 이름.

    @Builder
    public OrderItemDTO(Long profileImg, Long price, Long orderQuantity, String name, String color, Long discountPrice, String discountName, SizeLabel size) {
        this.profileImg = profileImg;
        this.price = price;
        this.orderQuantity = orderQuantity;
        this.name = name;
        this.color = color;
        this.discountPrice = discountPrice;
        this.discountName = discountName;
        this.size = size;
    }

}
