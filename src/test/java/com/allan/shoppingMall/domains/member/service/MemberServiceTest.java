package com.allan.shoppingMall.domains.member.service;

import com.allan.shoppingMall.common.config.aop.MemberJoinedEventPublishAop;
import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import com.allan.shoppingMall.domains.member.domain.model.MemberForm;
import com.allan.shoppingMall.domains.mileage.domain.model.MileageContent;
import com.allan.shoppingMall.domains.mileage.service.MileageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    MileageService mileageService;

    @InjectMocks
    MemberService memberService;

    @Test
    public void 가입_테스트() throws Exception {
        //given
        MemberForm TEST_MEMBER_FORM = new MemberForm();
        TEST_MEMBER_FORM.setAge(1);
        TEST_MEMBER_FORM.setName("testName");
        TEST_MEMBER_FORM.setAuthId("testAuthId");
        TEST_MEMBER_FORM.setPwd("testPwd12!!");
        TEST_MEMBER_FORM.setRePwd("testPwd12!!");
        TEST_MEMBER_FORM.setAddress("testAddr");
        TEST_MEMBER_FORM.setDetailAddress("testDetailAddr");
        TEST_MEMBER_FORM.setPostCode("testPostCode");
        TEST_MEMBER_FORM.setEmail("testEmail");
        TEST_MEMBER_FORM.setGender("1");
        TEST_MEMBER_FORM.setDateOfBirth("20000202");
        TEST_MEMBER_FORM.setPhone("23232323");

        //when
        memberService.join(TEST_MEMBER_FORM);

        //then
        verify(memberRepository, atLeastOnce()).save(any());
        verify(memberRepository, atLeastOnce()).findByAuthIdLike(any());
        verify(mileageService, atLeastOnce()).accumulateMileage("", "testAuthId", 3000l, MileageContent.JOIN_MILEAGE_ACCUMULATE);
    }
}
