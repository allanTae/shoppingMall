package com.allan.shoppingMall.domains.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 주문 도메인 진행 상태를 표시합니다.
 * ORDER_COMPLETE: 주문이 결제까지 완룐 된 상태.
 * ORDER_CANCEL: 주문이 취소 된 상태.
 * ORDER_ITEM_READY: 관리자가 주문내역을 확인하고 주문 접수가 완료 된 상태(상품 준비중 또는 상품 준비를 마치고 배송회사에 물건을 위탁한 상태이기에 주문이 실패 할 수 있습니다.)
 * ORDER_TEMP: 임시 주문 상태(결제 처리가 완료 되지 않은 주문 상태로, 결제 후 ORDER_ITEM_READY 상태가 된다).
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {
    ORDER_COMPLETE("주문완료"),
    ORDER_CANCEL("주문취소"),
    ORDER_ITEM_READY("상품준비중"),
    ORDER_TEMP("임시주문");

    private String desc;
}
