package com.allan.shoppingMall.common.config.security;

import com.allan.shoppingMall.domains.cart.domain.Cart;
import com.allan.shoppingMall.domains.cart.domain.CartRepository;
import com.allan.shoppingMall.domains.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.WebUtils;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SignInSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private CartService cartService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // cart 쿠키 정보 확인.
        Cookie cookie = WebUtils.getCookie(request, "cartCookie");

        // 비회원 카트(장바구니) 쿠키 정보가 존재한다면, 회원 카트(장바구니)에 업데이트 한다.
        if(cookie != null){
            String ckId = cookie.getValue();
            log.info("ckId: " + ckId);
            cartService.updateMemberCartByTempCart(ckId, authentication.getName());
            cookie.setValue(null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            cookie.setSecure(true);
            response.addCookie(cookie);
        }

        // 추후에 로그인 이후 이동 페이지로 변경 필요.
        response.sendRedirect(request.getContextPath() + "/index");
    }
}
