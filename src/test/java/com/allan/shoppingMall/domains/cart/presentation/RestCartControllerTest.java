package com.allan.shoppingMall.domains.cart.presentation;

import com.allan.shoppingMall.domains.cart.domain.model.CartRequest;
import com.allan.shoppingMall.domains.cart.domain.model.CartResult;
import com.allan.shoppingMall.domains.cart.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import javax.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = RestCartController.class
)
@AutoConfigureMockMvc
public class RestCartControllerTest {

    @MockBean
    CartService cartService;

    @Autowired
    MockMvc mvc;

    /**
     * 최초 비회원 장바구니에 상품을 등록하는 테스트 케이스입니다.
     * 비회원 장바구니가 존재하지 않는 환경입니다.(= 장바구니 쿠키 정보가 없는 환경입니다.)
     */
    @Test
    public void 비회원_장바구니_상품_등록_테스트_1() throws Exception {
        //given
        CartRequest TEST_CART_REQUEST = new CartRequest();

        doNothing().when(cartService).addTempCart(any());

        //when
        ResultActions resultActions = mvc.perform(post("/cart")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(asJsonString(TEST_CART_REQUEST)));

        //then
        verify(cartService, atLeastOnce()).addTempCart(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(cookie().exists("cartCookie"))
                .andExpect(cookie().maxAge("cartCookie", 60 * 60 * 24 *1))
                .andExpect(cookie().path("cartCookie", "/"))
                .andExpect(jsonPath("cartResultMessage").value(CartResult.ADD_CART_SUCCESS.getMessage()))
                .andExpect(jsonPath("cartResult").value(CartResult.ADD_CART_SUCCESS.getResult()));
    }

    /**
     * 기존 비회원 장바구니에 상품을 등록하는 테스트 케이스입니다.
     * 비회원 장바구니가 존재하는 환경입니다.(= 장바구니 쿠키 정보가 존재하는 환경입니다.)
     */
    @Test
    public void 비회원_장바구니_상품_등록_테스트_2() throws Exception {
        //given
        CartRequest TEST_CART_REQUEST = new CartRequest();

        Cookie TEST_COOKIE = new Cookie("cartCookie", "testCkId");
        TEST_COOKIE.setPath("/");
        TEST_COOKIE.setMaxAge(60 * 60 * 24 *1);

        doNothing().when(cartService).addTempCart(any());

        //when
        ResultActions resultActions = mvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(TEST_CART_REQUEST))
                .cookie(TEST_COOKIE));

        //then
        verify(cartService, atLeastOnce()).updatempCart(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(cookie().exists("cartCookie"))
                .andExpect(cookie().maxAge("cartCookie", 60 * 60 * 24 *1))
                .andExpect(cookie().path("cartCookie", "/"))
                .andExpect(jsonPath("cartResultMessage").value(CartResult.ADD_CART_SUCCESS.getMessage()))
                .andExpect(jsonPath("cartResult").value(CartResult.ADD_CART_SUCCESS.getResult()));
    }

    /**
     * 회원 장바구니에 상품을 등록하는 테스트 입니다.
     */
    @Test
    @WithMockUser
    public void 회원_장바구니_상품_등록_테스() throws Exception {
        //given
        CartRequest TEST_CART_REQUEST = new CartRequest();

        doNothing().when(cartService).addTempCart(any());

        //when
        ResultActions resultActions = mvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(TEST_CART_REQUEST)));

        //then
        verify(cartService, atLeastOnce()).addMemberCart(any(), any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("cartResultMessage").value(CartResult.ADD_CART_SUCCESS.getMessage()))
                .andExpect(jsonPath("cartResult").value(CartResult.ADD_CART_SUCCESS.getResult()));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
