package com.allan.shoppingMall.domains.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 주문완료 정보를 저장하는 DTO 클래스
 * 주문 서비스단에서 주문 유효성 검사 후 결제완료 주문 정보를 '주문완료' 이벤트에 전달하기 위한 DTO 클래스 입니다.
 */
@AllArgsConstructor
@Getter
public class CompletdOrderInfo {
    private Long orderId; // 결제 완료 한건의 주문 도메인의 id.
    private String authId; // 로그인한 아이디.
}
