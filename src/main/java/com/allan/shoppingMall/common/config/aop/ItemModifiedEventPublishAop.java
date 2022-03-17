package com.allan.shoppingMall.common.config.aop;

import com.allan.shoppingMall.domains.item.domain.item.ItemModifiedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * ClothesService, AccessoryService 에서 상품 정보가 변경 된 경우, 상품변경 이벤트가 발생하도록 하는 aop 클래스 입니다.
 */
@Aspect
@Component
@Profile("prod | devel")
@RequiredArgsConstructor
@Slf4j
public class ItemModifiedEventPublishAop {

    private final ApplicationContext context;

    @AfterReturning(pointcut = "execution(* com.allan.shoppingMall.domains.item.service.ClothesService.updateClothes(..))", returning = "returnValue")
    public void publishClothesModifiedEvent(Object returnValue){
        log.info("clothesModifiedEventAop execute!");
        ItemModifiedEvent event = new ItemModifiedEvent((Long) returnValue);
        context.publishEvent(event);
    }

    @AfterReturning(pointcut = "execution(* com.allan.shoppingMall.domains.item.service.AccessoryService.updateAccessory(..))", returning = "returnValue")
    public void publishAccessoryModifiedEvent(Object returnValue){
        log.info("accessoryModifiedEventAop execute!");
        ItemModifiedEvent event = new ItemModifiedEvent((Long) returnValue);
        context.publishEvent(event);
    }
}
