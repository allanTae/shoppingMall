package com.allan.shoppingMall.common.aop;

import com.allan.shoppingMall.common.config.aop.MemberJoinedEventPublishAop;
import com.allan.shoppingMall.domains.member.domain.MemberJoinedEvent;
import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import com.allan.shoppingMall.domains.member.domain.model.MemberForm;
import com.allan.shoppingMall.domains.member.service.MemberService;
import com.allan.shoppingMall.domains.mileage.service.MileageService;
import org.junit.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;

@SpringBootTest
public class MemberJoinEventPublishAopTest {

    @Test
    public void 회원가입_장바구니_생성프록시_테스트() throws Exception {
        //given
        MemberRepository TEST_MEMBER_REPOSITORY = mock(MemberRepository.class);
        MileageService TEST_MILEAGE_SERVICE = mock(MileageService.class);
        ApplicationContext TEST_APPLICATION_CONTEXT = mock(ApplicationContext.class);

        AspectJProxyFactory factory = new AspectJProxyFactory(new MemberService(TEST_MEMBER_REPOSITORY, TEST_MILEAGE_SERVICE));
        factory.addAspect(new MemberJoinedEventPublishAop(TEST_APPLICATION_CONTEXT));
        MemberService proxy = factory.getProxy();

        //when, then
        proxy.join(createMemberForm());

        // then
        verify(TEST_APPLICATION_CONTEXT, atLeastOnce()).publishEvent(any(MemberJoinedEvent.class));
    }

    private MemberForm createMemberForm() {
        MemberForm TEST_MEMBER_FORM = new MemberForm();
        TEST_MEMBER_FORM.setAge(1);
        TEST_MEMBER_FORM.setName("테스터");
        TEST_MEMBER_FORM.setAuthId("testAuthId");
        TEST_MEMBER_FORM.setPwd("testPwd12!!");
        TEST_MEMBER_FORM.setRePwd("testPwd12!!");
        TEST_MEMBER_FORM.setDetailAddress("testDetailAddr");
        TEST_MEMBER_FORM.setJibunAddress("testJibunAddr");
        TEST_MEMBER_FORM.setRoadAddress("testRoadAddr");
        TEST_MEMBER_FORM.setExtraAddress("testExtraAddr");
        TEST_MEMBER_FORM.setPostCode("testPostCode");
        TEST_MEMBER_FORM.setEmail("testEmail@test.tae");
        TEST_MEMBER_FORM.setGender("1");
        TEST_MEMBER_FORM.setDateOfBirth("20000202");
        TEST_MEMBER_FORM.setPhone("23232323");
        TEST_MEMBER_FORM.setYear("2021");
        TEST_MEMBER_FORM.setMonth("04");
        TEST_MEMBER_FORM.setDay("29");
        return TEST_MEMBER_FORM;
    }
}
