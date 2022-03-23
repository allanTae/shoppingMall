package com.allan.shoppingMall.domains.member.service;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.MemberNotFoundException;
import com.allan.shoppingMall.domains.cart.domain.Cart;
import com.allan.shoppingMall.domains.cart.domain.CartRepository;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberJoinedEvent;
import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 회원가입시, 회원가입 이벤트에 반응하는 handler 클래스입니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MemberJoinedEventHandler {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    @Async
    @EventListener
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void handleEvent(MemberJoinedEvent event){
        log.info("MemberJoinEnvent handler call!");
        log.info("thread: " + Thread.currentThread());

        Member member = memberRepository.findById(event.getMemberId()).orElseThrow(() ->
                new MemberNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if(member != null)
            cartRepository.save(Cart.builder().member(member).build());
    }

}
