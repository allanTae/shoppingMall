package com.allan.shoppingMall.domains.order.domain.model;

import com.allan.shoppingMall.domains.order.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Order 요약 정보를 담고 있는 DTO 클래스.
 * 서버측에서 사용자측으로 주문 요약 정보를 담고 있습니다.
 * (주문 목록 에 필요한 몇몇의 정보들만 포함하고 있습니다.)
 */
@Setter
@Getter
public class OrderSummaryDTO {
    private Long orderId; // 주문 번호.
    private Long orderTotalAmount; // 주문 총 가격.
    private Long profileImgId; // 주문 프로필 이미지(주문 상품의 프로필 이미지).
    private String orderName; // 메인 상품을 이용한 주문 이름.
    private String orderStatus; // 주문 상태.
    private String createdDate; // 주문 날자.

    @Builder
    public OrderSummaryDTO(Long orderId, Long orderTotalAmount, Long profileImgId, String orderName, String orderStatus, LocalDateTime createdDate) {
        this.orderId = orderId;
        this.orderTotalAmount = orderTotalAmount;
        this.profileImgId = profileImgId;
        this.orderName = orderName;
        this.orderStatus = orderStatus;
        setCreatedDate(createdDate);
    }

    private void setCreatedDate(LocalDateTime createdDate){
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "OrderSummaryDTO{" +
                "orderId=" + orderId +
                ", orderTotalAmount=" + orderTotalAmount +
                ", profileImgId=" + profileImgId +
                ", OrderItemName=" + orderName +
                ", orderStatus=" + orderStatus +
                ", createdDate=" + createdDate +
                '}';
    }

}
