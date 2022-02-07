package com.allan.shoppingMall.domains.cart.domain.model;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 프론트단에 장바구니 상품의 필수 옵션 정보를 포함한 클래스입니다.
 * 장바구니 내 상품을 등록 할시에, 장바구니 상품이 이미 장바구니에 등록된 상품인경우(itemId 가 동일한 상품 인경우) 필수정보만 추가 됩니다.
 * (CartItemDTO를 참고 해 주세요.)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequiredOption {
    private Long itemQuantity; // 장바구니 상품 수량.
    private SizeLabel itemSize; // 장바구니 상품 크기.
}
