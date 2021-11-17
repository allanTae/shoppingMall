package com.allan.shoppingMall.domains.member.presentation;

import com.allan.shoppingMall.common.api.status.MemberResponse;
import com.allan.shoppingMall.common.value.Address;
import com.allan.shoppingMall.domains.member.domain.Gender;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import com.allan.shoppingMall.domains.member.domain.MemberRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(
        controllers = RestMemberController.class
)
@WithMockUser
@AutoConfigureMockMvc
public class RestMemberControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    MemberRepository memberRepository;

    @Test
    public void 중복아이디_확인_테스트() throws Exception {
        //given
        Member TEST_MEMBER = createMember();
        CheckId TEST_API_REQUEST = new CheckId("checkId");
        given(memberRepository.findByAuthIdLike(any()))
                .willReturn(Optional.of(TEST_MEMBER));

        //when
        ResultActions resultActions = mvc.perform(post("/member/checkId")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(asJsonString(TEST_API_REQUEST)));

        //then
        verify(memberRepository, atLeastOnce()).findByAuthIdLike(any());
        resultActions
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    System.out.println("=========response============");
                    System.out.println(response.getContentAsString());
                    System.out.println("=============================");
                })
                .andExpect(jsonPath("status").value(MemberResponse.IN_USE.getStatus()))
                .andExpect(jsonPath("message").value(MemberResponse.IN_USE.getMessage()));
    }

    private class CheckId {

        public String authId;

        public String getAuthId(){
            return this.authId;
        }
        public CheckId(String authId){
            this.authId = authId;
        }
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Member createMember() {
        return Member.builder()
                .name("test_name")
                .age(10)
                .authId("test_authId")
                .pwd("test_pwd")
                .address(new Address("jibun_addr", "road_addr", "postCode", "detail_addr", "extra_addr"))
                .nickName("test_nickname")
                .phoneNumber("000-0000-0000")
                .gender(Gender.MAN)
                .email("test_email")
                .role(MemberRole.ACTIVATED_USER)
                .build();
    }

}
