package com.allan.shoppingMall.domains.delivery.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryStatus {
    DELIVERY_COMPLETE("배송완료"),
    SHIPPING("배송중"),
    DELIVERY_READY("배송준비중"),
    DELIVERY_CANCEL("배송취소");

    private String desc;
}
