package com.allan.shoppingMall.domains.cart.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 장바구니 상품의 필수 옵션 정보를 포함한 클래스입니다.
 */
@Getter
@Setter
@AllArgsConstructor
public class RequiredOption {
    private Long cartItemQuantity; // 장바구니 상품 수량.
    private SizeLabel itemSize; // 장바구니 상품 크기.
}
