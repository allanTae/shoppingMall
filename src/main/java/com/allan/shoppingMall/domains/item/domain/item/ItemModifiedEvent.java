package com.allan.shoppingMall.domains.item.domain.item;

import lombok.*;

/**
 * 상품 정보를 수정 할 때, 수정 이벤트 클래스입니다.
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
public class ItemModifiedEvent {
    private Long itemId; // 수정 된 Item 아이디.
}
