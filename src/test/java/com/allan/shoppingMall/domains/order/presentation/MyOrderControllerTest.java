package com.allan.shoppingMall.domains.order.presentation;

import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.order.domain.Order;
import com.allan.shoppingMall.domains.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(
        controllers = MyOrderController.class
)
@AutoConfigureMockMvc
@WithMockUser
public class MyOrderControllerTest {

    @MockBean
    OrderService orderService;

    @MockBean
    AuthenticationConverter authenticationConverter;

    @Autowired
    MockMvc mvc;

    // securityConfig.class 의 SignInSuccessHandler 에서 사용 할 cartService.
    @MockBean
    CartService cartService;

    @Test
    public void 자신의_주문_목록_테스트() throws Exception {
        //given
        Member TEST_MEMBER = Member.builder()
                .authId("testAuthId")
                .build();
        given(authenticationConverter.getMemberFromAuthentication(any()))
                .willReturn(TEST_MEMBER);

        Order TEST_ORDER = Order.builder().build();
        Page<Order> TEST_PAGE = new PageImpl<>(List.of(TEST_ORDER));
        given(orderService.getMyOrderSummaryList(any(), any()))
                .willReturn(TEST_PAGE);

        //when
        ResultActions resultActions = mvc.perform(get("/myOrder/list")
                                                    .param("page", "1"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("myOrderList"))
                .andExpect(model().attributeExists("pagination"))
                .andExpect(view().name("order/myOrderList"));
    }
}
