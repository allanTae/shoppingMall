package com.allan.shoppingMall.domains.order.domain.model;

import com.allan.shoppingMall.domains.order.domain.OrderStatus;
import com.allan.shoppingMall.domains.order.domain.OrdererInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 주문, 배송 상세 정보를 가지고 있는 객체.
 * 서버단에서 프론트단으로 전송하는 객체.
 */
@Getter
@Setter
public class OrderDetailDTO {

    // 주문 정보.
    private Long orderId;
    private String orderDate;

    // 주문 상품 정보.
    private List<OrderItemDTO> orderItems;

    // 배송 정보.
    private String recipient;
    private String recipientPhone;
    private String address;
    private String deliveryMemo;
    private String orderStatus;
    private boolean isCancelOrder; // 주문 취소 가능 여부.
    private OrdererInfo ordererInfo;

    @Builder
    public OrderDetailDTO(Long orderId, LocalDateTime orderDate, List<OrderItemDTO> orderItems, String recipient, String recipientPhone, String address, String deliveryMemo, OrdererInfo ordererInfo, String orderStatus) {
        this.orderId = orderId;
        orderDate(orderDate);
        this.orderItems = orderItems;
        this.recipient = recipient;
        this.recipientPhone = recipientPhone;
        this.address = address;
        this.deliveryMemo = deliveryMemo;
        this.ordererInfo = ordererInfo;
        setOrderStatus(orderStatus);
    }

    private void orderDate(LocalDateTime createdDate){
        this.orderDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void setOrderStatus(String orderStatus){
        this.orderStatus = orderStatus;
        if(orderStatus.equals(OrderStatus.ORDER_ITEM_READY.getDesc()))
            this.isCancelOrder = true;
        else
            this.isCancelOrder = false;
    }

    public boolean getIsCancelOrder(){
        return this.isCancelOrder;
    }
}
