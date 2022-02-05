package com.allan.shoppingMall.domains.cart.presentation;

import com.allan.shoppingMall.domains.cart.domain.Cart;
import com.allan.shoppingMall.domains.cart.domain.model.CartDTO;
import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.mileage.service.MileageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import javax.servlet.http.Cookie;
import static org.mockito.ArgumentMatchers.any;
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
    public void 비회원_장바구니_조회_테스트() throws Exception {
        //given
        CartDTO TEST_COOKIE_CART_DTO = new CartDTO();
        given(cartService.getCookieCart(any()))
                .willReturn(TEST_COOKIE_CART_DTO);

        Cookie TEST_COOKIE = new Cookie("cartCookie", "testCkId");
        TEST_COOKIE.setPath("/");
        TEST_COOKIE.setMaxAge(60 * 60 * 24 *1);

        //when
        ResultActions resultActions = mvc.perform(get("/cart/list")
                                                    .cookie(TEST_COOKIE));

        //then
        verify(cartService, atLeastOnce()).getCookieCart(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cartInfo"))
                .andExpect(view().name("cart/cartList"));
    }

    @Test
    @WithMockUser
    public void 회원_장바구니_조회테스트() throws Exception {
        //given
        CartDTO TEST_MEMBER_CART_DTO = new CartDTO();
        given(cartService.getMemberCart(any()))
                .willReturn(TEST_MEMBER_CART_DTO);

        Cookie TEST_COOKIE = new Cookie("cartCookie", "testCkId");
        TEST_COOKIE.setPath("/");
        TEST_COOKIE.setMaxAge(60 * 60 * 24 *1);

        //when
        ResultActions resultActions = mvc.perform(get("/cart/list")
                .cookie(TEST_COOKIE));

        //then
        verify(cartService, atLeastOnce()).getMemberCart(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cartInfo"))
                .andExpect(view().name("cart/cartList"));
    }

    @Test
    public void 회원_장바구니_주문요청_페이지_테스트() throws Exception {
        //given
        CartDTO TEST_CART_DTO = new CartDTO();

        //when

        //then
    }
}
