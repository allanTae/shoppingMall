package com.allan.shoppingMall.domains.order.service;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.ItemNotFoundException;
import com.allan.shoppingMall.common.exception.order.OrderNotFoundException;
import com.allan.shoppingMall.common.value.Address;
import com.allan.shoppingMall.domains.delivery.domain.Delivery;
import com.allan.shoppingMall.domains.delivery.domain.DeliveryStatus;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesRepository;
import com.allan.shoppingMall.domains.item.domain.ItemRepository;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSizeRepository;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.order.domain.*;
import com.allan.shoppingMall.domains.order.domain.model.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClothesRepository clothesRepository;
    private final ClothesSizeRepository clothesSizeRepository;

    /**
     * 상품 상세페이지를 통해서 바로 주문하는 경우 사용하는 주문 메소드.
     * (주문 리스트에 상품들은 모두 동일한 종류의 상품들이다. 예) 의상 페이지에서 주문시, 주문 리스트 상품은 모두 의상 상품들.)
     */
    @Transactional
    public Long order(OrderRequest request, Member member){
        // 카테고리에 따른 분기 처리가 필요. 카테고리 추가시 코드 수정이 필요하다.

        Order order = Order.builder()
                .orderer(member)
                .delivery(Delivery.builder()
                        .address(Address.builder()
                                .roadAddress(request.getAddress())
                                .detailAddress(request.getDetailAddress())
                                .postCode(request.getPostcode())
                                .build())
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .build())
                .orderStatus(OrderStatus.ORDER_ITEM_READY)
                .ordererInfo(OrdererInfo.builder()
                                .ordererName(request.getOrdererName())
                                .ordererPhone(request.getOrdererPhone())
                                .ordererEmail(request.getOrdererEmail())
                                .build())
                .build();

        List<OrderItem> orderItems = request.getOrderItems()
                .stream()
                .map(orderLineRequest -> {
                    // findItem 의 카테고리를 확인 해서 분기 처리가 필요하다.(카테고리 추가 후 로직 변경 필요.)
                    // clothes 상품인 경우.
                    Clothes clothes = clothesRepository.findById(orderLineRequest.getItemId()).orElseThrow(()
                            -> new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND.getMessage(), ErrorCode.ENTITY_NOT_FOUND));

                    ClothesSize clothesSize = clothesSizeRepository.getClothesSizebySizelabel(clothes, orderLineRequest.getSize());

                    return new OrderClothes(orderLineRequest.getOrderQuantity(), clothes, clothesSize);
                    // 그외의 상품인 경우.
                }).collect(Collectors.toList());

        order.changeOrderItems(orderItems);
        orderRepository.save(order);

        return order.getOrderId();
    }

    @Transactional
    public Long cancelOrder(Long orderId){
        Order findOrder = orderRepository.findById(orderId).orElseThrow(()
                -> new OrderNotFoundException(ErrorCode.ENTITY_NOT_FOUND.getMessage(), ErrorCode.ENTITY_NOT_FOUND));

        findOrder.cancelOrder();

        return findOrder.getOrderId();
    }
}
