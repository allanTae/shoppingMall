package com.allan.shoppingMall.domains.order.service;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.cart.CartNotFoundException;
import com.allan.shoppingMall.common.exception.order.OrderNotFoundException;
import com.allan.shoppingMall.domains.cart.domain.Cart;
import com.allan.shoppingMall.domains.cart.domain.CartItem;
import com.allan.shoppingMall.domains.cart.domain.CartRepository;
import com.allan.shoppingMall.domains.order.domain.Order;
import com.allan.shoppingMall.domains.order.domain.OrderCompletedEvent;
import com.allan.shoppingMall.domains.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문완료시, 주문결제 완료 이벤트에 반응하는 handler 클래스입니다.
 * 주문완료시, 결제된 상품을 장바구니에서 차감합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCompletedEventHandler {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    @Async
    @EventListener
    @Transactional
    public void handleEvent(OrderCompletedEvent event){
        log.info("OrderCompletedEvent handler call!");
        log.info("thread: " + Thread.currentThread());

        Order findOrder = orderRepository.findById(event.getOrderId()).orElseThrow(()
                -> new OrderNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        List<CartItem> cartItems = findOrder.getOrderItems().stream()
                .map(orderItem -> {
                    return CartItem.builder()
                            .item(orderItem.getItem())
                            .cartQuantity(orderItem.getOrderQuantity())
                            .size(orderItem.getItemSize().getSizeLabel())
                            .build();
                }).collect(Collectors.toList());

        Cart findCart = cartRepository.findByAuthId(event.getAuthId()).orElseThrow(()
                -> new CartNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        findCart.substractCartItems(cartItems);
    }
}
