package com.allan.shoppingMall.domains.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 주문 도메인 진행 상태를 표시합니다.
 * ORDER_COMPLETE: 주문이 배송까지 완료 된 상태.
 * ORDER_CANCEL: 주문이 취소 된 상태.
 * ORDER_READY: 관리자가 주문내역을 확인하고 주문 접수가 완료 된 상태(상품 준비를 마치고 배송회사에 물건을 위탁한 상태).
 * ORDER_ITEM_READY: 주문 접수 대기 상태, 주문에 대한 결제가 완료 된 상태(관리자가 주문을 확인하지 않은 상태이며 고객이 직접 주문이 취소가 가능한 상태).
 * ORDER_TEMP: 임시 주문 상태(결제 처리가 완료 되지 않은 주문 상태로, 결제 후 ORDER_ITEM_READY 상태가 된다).
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {
    ORDER_COMPLETE("주문완료"),
    ORDER_CANCEL("주문취소"),
    ORDER_READY("주문접수"),
    ORDER_ITEM_READY("상품준비중"),
    ORDER_TEMP("임시주문");

    private String desc;
}
