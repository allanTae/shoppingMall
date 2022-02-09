package com.allan.shoppingMall.common.config.aop;

import com.allan.shoppingMall.domains.member.domain.MemberJoinedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * MemberService의 회원가입 메소드에 aop를 설정하여 회원가입 이벤트가 발생하도록 하는 aop 클래스 입니다.
 */
@Aspect
@Component
@Profile("prod | devel")
@RequiredArgsConstructor
@Slf4j
public class MemberJoinedEventPublishAop {
    private final ApplicationContext context;

    @AfterReturning(pointcut = "execution(* com.allan.shoppingMall.domains.member.service.MemberService.join(..))", returning = "returnValue")
    public void publishMemberJoinedEvent(Object returnValue){
        log.info("memberJoinedEventAop execute!");
        MemberJoinedEvent event = new MemberJoinedEvent((Long) returnValue);
        context.publishEvent(event);
    }

}
