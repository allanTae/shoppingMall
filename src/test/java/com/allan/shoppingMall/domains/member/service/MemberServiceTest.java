package com.allan.shoppingMall.domains.member.service;

import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import com.allan.shoppingMall.domains.member.domain.model.MemberForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

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
        TEST_MEMBER_FORM.setDetailAddress("testDetailAddr");
        TEST_MEMBER_FORM.setJibunAddress("testJibunAddr");
        TEST_MEMBER_FORM.setRoadAddress("testRoadAddr");
        TEST_MEMBER_FORM.setExtraAddress("testExtraAddr");
        TEST_MEMBER_FORM.setPostCode("testPostCode");
        TEST_MEMBER_FORM.setEmail("testEmail");
        TEST_MEMBER_FORM.setGender("1");
        TEST_MEMBER_FORM.setDateOfBirth("20000202");
        TEST_MEMBER_FORM.setPhone("23232323");

        //when
        memberService.join(TEST_MEMBER_FORM);

        //then
        verify(memberRepository, atLeastOnce()).save(any());
        verify(memberRepository, atLeastOnce()).findByAuthId(any());
    }
}
