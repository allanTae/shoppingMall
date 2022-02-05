package com.allan.shoppingMall.domains.cart.presentation;

import com.allan.shoppingMall.common.domain.model.UserInfo;
import com.allan.shoppingMall.domains.cart.domain.model.CartDTO;
import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.mileage.service.MileageService;
import com.allan.shoppingMall.domains.order.domain.model.OrderSummaryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final MileageService mileageService;
    private final AuthenticationConverter authenticationConverter;

    @GetMapping("/list")
    public String cartList(@CookieValue(value = "cartCookie", required = false) Cookie cartCookie, Model model, Authentication authentication){
        // 비회원 장바구니
        CartDTO cartDTO = new CartDTO();
        if(authentication == null){
            if(cartCookie != null){
                String ckId = cartCookie.getValue();
                cartDTO = cartService.getCookieCart(ckId);
            }

        }
        // 회원 장바구니
        else if(authentication != null){
            cartDTO = cartService.getMemberCart(authentication.getName());
        }
        model.addAttribute("cartInfo", cartDTO);
        return "cart/cartList";
    }

    /**
     * 상품 아이디를 통해 장바구니 도메인으로 주문페이지에 전달 할 주문정보로 변환하여 요청하는 메소드.
     * @param itemId
     * @return
     */
    @GetMapping("/{itemId}/order")
    public String cartOrder(@CookieValue(value = "cartCookie", required = false) Cookie cartCookie, Model model, Authentication authentication, @PathVariable("itemId") Long itemId){
        OrderSummaryRequest orderSummaryRequest = new OrderSummaryRequest();
        if(authentication == null){
            if(cartCookie != null){
                String ckId = cartCookie.getValue();
                orderSummaryRequest = cartService.transferOrderSummary(cartService.getCookieCart(ckId), itemId);
            }

        }
        // 회원 장바구니
        else if(authentication != null){
            orderSummaryRequest = cartService.transferOrderSummary(cartService.getMemberCart(authentication.getName()), itemId);
        }

        // 주문요청 정보 및 사용자 정보.
        model.addAttribute("orderInfo", orderSummaryRequest);
        setUserInfo(model, authentication);

        // 마일리지 정보.
        long availableMileagePoint = mileageService.getAvailableMileagePoint(authentication.getName());
        model.addAttribute("availableMileage", availableMileagePoint);

        return "order/orderForm";
    }

    /**
     * 로그인한 계정의 정보를 전달하는 메소드.
     * @param model
     * @param authentication
     */
    private void setUserInfo(Model model, Authentication authentication){
        Member findMember = authenticationConverter.getMemberFromAuthentication(authentication);
        model.addAttribute("userInfo", UserInfo.builder()
                .memberId(findMember.getMemberId())
                .name(findMember.getName())
                .email(findMember.getEmail())
                .phone(findMember.getPhone())
                .build());
    }
}
