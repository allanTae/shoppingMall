package com.allan.shoppingMall.common.config.aop;

import com.allan.shoppingMall.domains.member.domain.MemberJoinedEvent;
import com.allan.shoppingMall.domains.order.domain.OrderCompletedEvent;
import com.allan.shoppingMall.domains.order.domain.model.CompletdOrderInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * orderService의 주문 완료 후, 주문 완료 이벤트를 발생 시키는 AOP event publish 클래스.
 */
@Aspect
@Component
@Profile("prod | devel")
@RequiredArgsConstructor
@Slf4j
public class OrderCompletedPublishAop {
    private final ApplicationContext context;

    @AfterReturning(pointcut = "execution(* com.allan.shoppingMall.domains.order.service.OrderService.validatePaymentByIamport(..))", returning = "returnValue")
    public void publishMemberJoinedEvent(Object returnValue){
        log.info("orderCompletedPublishAop execute!");
        CompletdOrderInfo completdOrderInfo = null;
        if( returnValue instanceof CompletdOrderInfo)
            completdOrderInfo = (CompletdOrderInfo) returnValue;

        log.info("authId: " + completdOrderInfo.getAuthId());
        log.info("orderId: " + completdOrderInfo.getOrderId());
        OrderCompletedEvent event = new OrderCompletedEvent(completdOrderInfo.getOrderId(), completdOrderInfo.getAuthId());
        context.publishEvent(event);
    }
}
