package com.allan.shoppingMall.domains.cart.presentation;

import com.allan.shoppingMall.domains.cart.domain.model.CartDTO;
import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.mileage.service.MileageService;
import com.allan.shoppingMall.domains.order.domain.model.OrderSummaryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import javax.servlet.http.Cookie;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CartController.class
)
@AutoConfigureMockMvc
public class CartControllerTest {

    @MockBean
    CartService cartService;

    @MockBean
    MileageService mileageService;

    @MockBean
    AuthenticationConverter authenticationConverter;

    @Autowired
    MockMvc mvc;

    @Test
    public void 장바구니_페이지_테스트() throws Exception {
        //given
        CartDTO TEST_COOKIE_CART_DTO = new CartDTO();
        given(cartService.getCookieCart(any()))
                .willReturn(TEST_COOKIE_CART_DTO);

        //when
        ResultActions resultActions = mvc.perform(get("/cart"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("cart/cartList"));
    }

    @Test
    @WithMockUser
    public void 회원_장바구니_단일_상품_주문페이지_테스트() throws Exception {
        //given
        CartDTO TEST_MEMBER_CART_DTO = new CartDTO();
        given(cartService.getMemberCart(any()))
                .willReturn(TEST_MEMBER_CART_DTO);

        OrderSummaryRequest TEST_ORDER_SUMMARY_REQUEST = new OrderSummaryRequest();
        given(cartService.transferOrderSummary(any(), anyLong()))
                .willReturn(TEST_ORDER_SUMMARY_REQUEST);


        Cookie TEST_COOKIE = new Cookie("cartCookie", "testCkId");
        TEST_COOKIE.setPath("/");
        TEST_COOKIE.setMaxAge(60 * 60 * 24 *1);

        Member TEST_MEMBER = Member.builder().build();
        given(authenticationConverter.getMemberFromAuthentication(any()))
                .willReturn(TEST_MEMBER);

        Long TEST_MILEAGE = 200l;
        given(mileageService.getAvailableMileagePoint(any()))
                .willReturn(TEST_MILEAGE);

        //when
        ResultActions resultActions = mvc.perform(get("/cart/1/order")
                .cookie(TEST_COOKIE));

        //then
        verify(cartService, atLeastOnce()).getMemberCart(any());
        verify(cartService, atLeastOnce()).transferOrderSummary(any(), anyLong());
        verify(authenticationConverter, atLeastOnce()).getMemberFromAuthentication(any());
        verify(mileageService, atLeastOnce()).getAvailableMileagePoint(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orderInfo"))
                .andExpect(model().attributeExists("userInfo"))
                .andExpect(model().attributeExists("availableMileage"))
                .andExpect(view().name("order/orderForm"));
    }

    @Test
    @WithMockUser
    public void 회원_장바구니_모든상품_주문페이지_테스트() throws Exception {
        //given
        CartDTO TEST_MEMBER_CART_DTO = new CartDTO();
        given(cartService.getMemberCart(any()))
                .willReturn(TEST_MEMBER_CART_DTO);

        OrderSummaryRequest TEST_ORDER_SUMMARY_REQUEST = new OrderSummaryRequest();
        given(cartService.transferOrderSummary(any()))
                .willReturn(TEST_ORDER_SUMMARY_REQUEST);


        Cookie TEST_COOKIE = new Cookie("cartCookie", "testCkId");
        TEST_COOKIE.setPath("/");
        TEST_COOKIE.setMaxAge(60 * 60 * 24 *1);

        Member TEST_MEMBER = Member.builder().build();
        given(authenticationConverter.getMemberFromAuthentication(any()))
                .willReturn(TEST_MEMBER);

        Long TEST_MILEAGE = 200l;
        given(mileageService.getAvailableMileagePoint(any()))
                .willReturn(TEST_MILEAGE);

        //when
        ResultActions resultActions = mvc.perform(get("/cart/order")
                .cookie(TEST_COOKIE));

        //then
        verify(cartService, atLeastOnce()).getMemberCart(any());
        verify(cartService, atLeastOnce()).transferOrderSummary(any());
        verify(authenticationConverter, atLeastOnce()).getMemberFromAuthentication(any());
        verify(mileageService, atLeastOnce()).getAvailableMileagePoint(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orderInfo"))
                .andExpect(model().attributeExists("userInfo"))
                .andExpect(model().attributeExists("availableMileage"))
                .andExpect(view().name("order/orderForm"));
    }
}
