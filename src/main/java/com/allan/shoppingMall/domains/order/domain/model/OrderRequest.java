package com.allan.shoppingMall.domains.order.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private Long itemId;
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

}
