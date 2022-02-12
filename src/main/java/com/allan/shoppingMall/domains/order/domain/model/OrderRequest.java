package com.allan.shoppingMall.domains.order.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 주문상세창에서(orderForm.jsp)에서 주문 도메인 생성을 위해
 * 서버측에 전달하는 주문정보를 가지고 있는 객체입니다.
 */
@Getter
@Setter
public class OrderRequest {
    // 주문 상품 정보.
    private List<OrderLineRequest> orderItems;

    // 주문자 정보.
    private String ordererName;
    private String ordererPhone;
    private String ordererEmail;

    // 배송 정보.
    private String recipientName;
    private String recipientPhone;
    private String postcode;
    private String roadAddress;
    private String jibunAddress;

    @NotEmpty(message = "상세 주소를 입력 해 주세요.")
    private String detailAddress;
    private String deliveryMemo;

    @NotEmpty(message = "주소를 입력 해 주세요.")
    private String address;

    // 마일리지 정보.
    @Min(message = "나이를 제대로 입력 해 주세요.", value = 0)
    private Long usedMileage;

    @Override
    public String toString() {
        return "OrderRequest{" +
                "orderItems=" + orderItems.toString() +
                ", ordererName='" + ordererName + '\'' +
                ", ordererPhone='" + ordererPhone + '\'' +
                ", ordererEmail='" + ordererEmail + '\'' +
                ", recipientName='" + recipientName + '\'' +
                ", recipientPhone='" + recipientPhone + '\'' +
                ", postcode='" + postcode + '\'' +
                ", roadAddress='" + roadAddress + '\'' +
                ", jibunAddress='" + jibunAddress + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", deliveryMemo='" + deliveryMemo + '\'' +
                ", usedMileage='" + usedMileage + '\'' +
                '}';
    }
}
