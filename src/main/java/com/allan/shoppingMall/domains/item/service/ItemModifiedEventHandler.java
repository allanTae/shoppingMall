package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.MemberNotFoundException;
import com.allan.shoppingMall.common.exception.item.ItemNotFoundException;
import com.allan.shoppingMall.domains.cart.domain.Cart;
import com.allan.shoppingMall.domains.cart.domain.CartItemRepository;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import com.allan.shoppingMall.domains.item.domain.item.ItemModifiedEvent;
import com.allan.shoppingMall.domains.item.domain.item.ItemRepository;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberJoinedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품 정보를 수정 할 때, 수정 이벤트 반응하는 handler 클래스입니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemModifiedEventHandler {
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    @Async
    @EventListener
    public void handleEvent(ItemModifiedEvent event){
        log.info("ItemModifiedEventHandler handler call!");
        log.info("thread: " + Thread.currentThread());

        Item findItem = itemRepository.findById(event.getItemId()).orElseThrow(() ->
                new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        List<SizeLabel> sizes = findItem.getItemSizes()
                .stream()
                .map(itemSize -> {
                    return itemSize.getSizeLabel();
                }).collect(Collectors.toList());

        // 변경 된 상품에서 존재하지 않는 사이즈를 담고 있는 장바구니 상품 도메인 삭제.
        cartItemRepository.deleteCartItems(findItem, sizes);
    }
}
