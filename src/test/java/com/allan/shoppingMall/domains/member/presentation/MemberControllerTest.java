package com.allan.shoppingMall.domains.member.presentation;

import com.allan.shoppingMall.common.config.security.SecurityConfig;
import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.member.domain.model.MemberForm;
import com.allan.shoppingMall.domains.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = MemberController.class
)
@WithMockUser
@AutoConfigureMockMvc
public class MemberControllerTest {

    @MockBean
    MemberService memberService;

    @Autowired
    MockMvc mvc;

    // securityConfig.class 의 SignInSuccessHandler 에서 사용 할 cartService.
    @MockBean
    CartService cartService;

    @Test
    public void 회원가입폼_테스트() throws Exception {
        //when
        ResultActions resultActions = mvc.perform(get("/member/signupForm"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("memberForm"))
                .andExpect(model().attributeExists("genders"))
                .andExpect(view().name("member/signupForm"));
    }

    @Test
    public void 가입_테스트() throws Exception {
        //given
        MemberForm TEST_MEMBER_FORM = createMemberForm();

        //when
        ResultActions resultActions = mvc.perform(post("/member")
                                            .flashAttr("memberForm", TEST_MEMBER_FORM)
                                            .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        //then
        verify(memberService, atLeastOnce()).join(any());
        resultActions
                .andExpect(status().is3xxRedirection());
    }

    private MemberForm createMemberForm() {
        MemberForm TEST_MEMBER_FORM = new MemberForm();
        TEST_MEMBER_FORM.setAge(1);
        TEST_MEMBER_FORM.setName("테스터");
        TEST_MEMBER_FORM.setAuthId("testAuthId");
        TEST_MEMBER_FORM.setPwd("testPwd12!!");
        TEST_MEMBER_FORM.setRePwd("testPwd12!!");
        TEST_MEMBER_FORM.setPostCode("testPostCode");
        TEST_MEMBER_FORM.setAddress("testAddress");
        TEST_MEMBER_FORM.setDetailAddress("testDetailAddress");
        TEST_MEMBER_FORM.setEmail("testEmail@test.tae");
        TEST_MEMBER_FORM.setGender("1");
        TEST_MEMBER_FORM.setDateOfBirth("20000202");
        TEST_MEMBER_FORM.setPhone("010-1111-2222");
        TEST_MEMBER_FORM.setYear("2021");
        TEST_MEMBER_FORM.setMonth("04");
        TEST_MEMBER_FORM.setDay("29");
        return TEST_MEMBER_FORM;
    }
}
