package com.allan.shoppingMall.domains.cart.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 장바구니 상품에서 필수옵션 정보를 클래스입니다.
 */
@Getter
@Setter
@AllArgsConstructor
public class RequiredOption {
    private SizeLabel size;
    private Long quantity;
}
