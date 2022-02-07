package com.allan.shoppingMall.domains.cart.presentation;

import com.allan.shoppingMall.common.domain.model.UserInfo;
import com.allan.shoppingMall.domains.cart.domain.model.CartDTO;
import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.mileage.service.MileageService;
import com.allan.shoppingMall.domains.order.domain.Order;
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
        CartDTO cartDTO = getCartDTO(authentication, cartCookie);
        model.addAttribute("cartInfo", cartDTO);
        return "cart/cartList";
    }

    /**
     * 장바구니 정보를 조회하여 사용자가 요청한 하나의 상품만 주문정보(OrderSummaryRequest)로 반환하여 주문페이지로 전달하는 메소드.
     * 주문정보는 (OrderSummaryRequest)를 참고하시길 바랍니다.
     * (현재는 주문페이지는 회원만 처리되도록 웹애플리케이션을 구성하였습니다. 추후에 비회원 결제도 추가하게 되면 테스트 코드 추가 해주세요.)
     * @param cartCookie 장바구니 쿠키 정보.
     * @param itemId 상품 도메인 아이디.
     * @return
     */
    @GetMapping("/{itemId}/order")
    public String orderCartItem(@CookieValue(value = "cartCookie", required = false) Cookie cartCookie, Model model, Authentication authentication, @PathVariable("itemId") Long itemId){
        // 주문 요청 정보.
        OrderSummaryRequest orderSummaryRequest = cartService.transferOrderSummary(getCartDTO(authentication, cartCookie), itemId);
        model.addAttribute("orderInfo", orderSummaryRequest);

        //사용자 정보.
        setUserInfo(model, authentication);

        // 마일리지 정보.
        model.addAttribute("availableMileage", mileageService.getAvailableMileagePoint(authentication.getName()));

        return "order/orderForm";
    }

    /**
     * 장바구니에 들어간 모든 상품들을 주문정보(OrderSummaryRequest)로 변환하여 주문페이지로 전달하는 메소드.
     * 주문정보는 (OrderSummaryRequest)를 참고 하시길 바랍니다.
     * (현재는 주문페이지는 회원만 처리되도록 웹애플리케이션을 구성하였습니다. 추후에 비회원 결제도 추가하게 되면 테스트 코드 추가 해주세요.)
     * @param cartCookie 장바구니 쿠키 정보.
     * @return
     */
    @GetMapping("/order")
    public String orderCartItems(@CookieValue(value = "cartCookie", required = false) Cookie cartCookie, Model model, Authentication authentication){
        // 주문 요청 정보.
        OrderSummaryRequest orderSummaryRequest = cartService.transferOrderSummary(getCartDTO(authentication, cartCookie));
        model.addAttribute("orderInfo", orderSummaryRequest);

        // 사용자 정보.
        setUserInfo(model, authentication);

        // 마일리지 정보.
        model.addAttribute("availableMileage", mileageService.getAvailableMileagePoint(authentication.getName()));

        return "order/orderForm";
    }

    /**
     * 장바구니를 조회하여 CartDTO 를 조회하는 메소드.
     * @param authentication
     * @param cartCookie 비회원 장바구니 쿠키 정보.
     * @return
     */
    private CartDTO getCartDTO(Authentication authentication, Cookie cartCookie){
        // 비회원 장바구니.
        if(authentication == null){
            if(cartCookie != null){
                String ckId = cartCookie.getValue();
                return cartService.getCookieCart(ckId);
            }
        }
        // 회원 장바구니.
        else if(authentication != null){
            return cartService.getMemberCart(authentication.getName());
        }
        return null;
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
