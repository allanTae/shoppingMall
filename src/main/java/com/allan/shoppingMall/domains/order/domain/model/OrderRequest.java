package com.allan.shoppingMall.domains.order.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 사용자가 서버측에 전달하는 주문정보를 가지고 있는 객체입니다.
 */
@Getter
@Setter
public class OrderRequest {
    private List<OrderLineRequest> orderItems;

    // 주문자 정보.
    private String ordererName;
    private String ordererPhone;
    private String ordererEmail;

    // 배송 정보.
    private String recipient;
    private String recipientPhone;
    private String postcode;
    private String address;
    private String detailAddress;
    private String deliveryMemo;

    @Override
    public String toString() {
        return "OrderRequest{" +
                "orderItems=" + orderItems.toString() +
                ", ordererName='" + ordererName + '\'' +
                ", ordererPhone='" + ordererPhone + '\'' +
                ", ordererEmail='" + ordererEmail + '\'' +
                ", recipient='" + recipient + '\'' +
                ", recipientPhone='" + recipientPhone + '\'' +
                ", postcode='" + postcode + '\'' +
                ", address='" + address + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", deliveryMemo='" + deliveryMemo + '\'' +
                '}';
    }
}
