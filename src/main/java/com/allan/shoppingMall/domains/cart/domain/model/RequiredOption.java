package com.allan.shoppingMall.domains.cart.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 장바구니 상품의 필수 옵션 정보를 포함한 클래스입니다.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequiredOption {
    private Long itemQuantity; // 장바구니 상품 수량.
    private SizeLabel itemSize; // 장바구니 상품 크기.
}
