package com.allan.shoppingMall.domains.cart.presentation;

import com.allan.shoppingMall.common.exception.ItemNotFoundException;
import com.allan.shoppingMall.common.exception.cart.CartModifyFailException;
import com.allan.shoppingMall.common.exception.cart.CartNotFoundException;
import com.allan.shoppingMall.domains.cart.domain.model.CartErrorResponse;
import com.allan.shoppingMall.domains.cart.domain.model.CartRequest;
import com.allan.shoppingMall.domains.cart.domain.model.CartResponse;
import com.allan.shoppingMall.domains.cart.domain.model.CartResult;
import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestCartController {

    private final CartService cartService;

    @ResponseBody
    @PostMapping("/cart")
    public ResponseEntity<CartResponse> addCart(@CookieValue(value = "cartCookie", required = false) Cookie cartCookie, @RequestBody CartRequest cartRequest, HttpServletRequest request, HttpServletResponse response, Authentication authentication){

        try{
            // 비회원 장바구니 클릭시, 쿠키가 존재하지 않는 경우(최초 비회원 장바구니 추가 할 경우)
            log.info("cartCookie: " + cartCookie);
            log.info("authentication: " + authentication);
            if(cartCookie == null && authentication == null){
                String ckid = RandomStringUtils.random(6, true, true);
                Cookie cookie = new Cookie("cartCookie", ckid);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 24 *1); // 유효기간 하루.
                response.addCookie(cookie);
                cartRequest.setCartCkId(ckid);
                cartService.addTempCart(cartRequest);

                return new ResponseEntity<CartResponse>(new CartResponse(CartResult.ADD_CART_SUCCESS), HttpStatus.OK);
            }
            // 비회원 장바구니 클릭시 쿠키가 이미 존재하는 경우(비회원 장바구니 추가 할때 이미 비회원 장바구니가 존재하는 경우)
            else if(cartCookie != null && authentication == null){
                String ckId = cartCookie.getValue();
                cartRequest.setCartCkId(ckId);

                try{
                    cartService.updatempCart(cartRequest);
                }catch (CartNotFoundException exception){
                    return new ResponseEntity<CartResponse>(new CartResponse(CartResult.ADD_CART_FAIL, CartErrorResponse.of(exception.getErrorCode())),
                            HttpStatus.OK);
                }

                cartCookie.setPath("/");
                cartCookie.setMaxAge(60 * 60 * 24 *1); // 유효기간 하루.
                response.addCookie(cartCookie);

                return new ResponseEntity<CartResponse>(new CartResponse(CartResult.ADD_CART_SUCCESS), HttpStatus.OK);
            }
            // 회원 장바구니 클릭시
            else if(authentication != null){
                cartService.addMemberCart(cartRequest, authentication.getName());
            }
        }catch (ItemNotFoundException exception){
            return new ResponseEntity<CartResponse>(new CartResponse(CartResult.ADD_CART_FAIL, CartErrorResponse.of(exception.getErrorCode())),
                    HttpStatus.OK);
        }

        return new ResponseEntity<CartResponse>(new CartResponse(CartResult.ADD_CART_SUCCESS),HttpStatus.OK);
    }

    /**
     * @param cartId 장바구니 도메인 id.
     * @param cartRequest 장바구니 요청 정보.
     * @return
     */
    @ResponseBody
    @PutMapping( value = "/cart/{cartId}")
    public ResponseEntity<CartResponse> modifyCart(@PathVariable("cartId") Long cartId, @RequestBody CartRequest cartRequest){
        try {
            cartService.modifyCart(cartRequest, cartId);
        }catch (CartModifyFailException exception){
            return new ResponseEntity<CartResponse>(new CartResponse(CartResult.MODIFY_CART_FAIL, CartErrorResponse.of(exception.getErrorCode())),
                    HttpStatus.OK);
        }
        return new ResponseEntity<CartResponse>(new CartResponse(CartResult.MODIFY_CART_SUCCESS), HttpStatus.OK);
    }
}
