package com.allan.shoppingMall.domains.cart.presentation;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.item.ItemNotFoundException;
import com.allan.shoppingMall.common.exception.cart.CartModifyFailException;
import com.allan.shoppingMall.common.exception.cart.CartNotFoundException;
import com.allan.shoppingMall.domains.cart.domain.model.CartDTO;
import com.allan.shoppingMall.domains.cart.domain.model.CartRequest;
import com.allan.shoppingMall.domains.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestCartController {

    private final CartService cartService;

    /** 장바구니 생성 메소드.
     * @param cartCookie
     * @param cartRequest
     * @param response
     * @param authentication
     * @return
     */
    @ResponseBody
    @PostMapping("/cart")
    public ResponseEntity<CartResponse> addCart(@CookieValue(value = "cartCookie", required = false) Cookie cartCookie, @RequestBody CartRequest cartRequest, HttpServletResponse response, Authentication authentication){
        try{
            // 비회원 장바구니 클릭시, 쿠키가 존재하지 않는 경우(최초 비회원 장바구니 추가 할 경우)
            if(cartCookie == null && authentication == null){
                String ckid = RandomStringUtils.random(6, true, true);
                Cookie cookie = new Cookie("cartCookie", ckid);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 24 *1); // 유효기간 이틀.
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
     * 장바구니 수정 메소드.
     * @param cartCookie
     * @param cartRequest
     * @param response
     * @param authentication
     * @return
     */
    @ResponseBody
    @PutMapping( value = "/cart")
    public ResponseEntity<CartResponse> modifyCartTest(@CookieValue(value = "cartCookie", required = false) Cookie cartCookie, @RequestBody CartRequest cartRequest, HttpServletResponse response, Authentication authentication){
        try{
            // 비회원 장바구니 클릭시, 쿠키가 존재하지 않는 경우(최초 비회원 장바구니 추가 할 경우)
            if(cartCookie == null && authentication == null){
                log.error("존재하지 않는 비회원 장바구니 수정 요청.");
                new ResponseEntity<CartResponse>(new CartResponse(CartResult.MODIFY_CART_FAIL, CartErrorResponse.of(ErrorCode.TEMP_CART_NOT_FOUND)),
                        HttpStatus.OK);
            }
            // 비회원 장바구니 클릭시 쿠키가 이미 존재하는 경우(비회원 장바구니 추가 할때 이미 비회원 장바구니가 존재하는 경우)
            else if(cartCookie != null && authentication == null){
                String ckId = cartCookie.getValue();
                cartService.modifyTempCart(cartRequest, ckId);

                cartCookie.setPath("/");
                cartCookie.setMaxAge(60 * 60 * 24 *1); // 유효기간 하루.
                response.addCookie(cartCookie);

                return new ResponseEntity<CartResponse>(new CartResponse(CartResult.MODIFY_CART_SUCCESS), HttpStatus.OK);
            }
            // 회원 장바구니 클릭시
            else if(authentication != null){
                cartService.modifyMemberCart(cartRequest, authentication.getName());
            }
        }catch (CartModifyFailException exception){
            return new ResponseEntity<CartResponse>(new CartResponse(CartResult.MODIFY_CART_FAIL, CartErrorResponse.of(exception.getErrorCode())),
                    HttpStatus.OK);
        }

        return new ResponseEntity<CartResponse>(new CartResponse(CartResult.MODIFY_CART_SUCCESS),HttpStatus.OK);
    }

    /**
     * 장바구니 조회 메소드.
     * @param cartCookie
     * @param model
     * @param authentication
     * @param response
     * @return
     */
    @GetMapping("/cart/list")
    public ResponseEntity<CartResponse> cartList(@CookieValue(value = "cartCookie", required = false) Cookie cartCookie, Model model, Authentication authentication,  HttpServletResponse response){
        // 비회원 장바구니
        CartDTO cartDTO = null;
        try {
            cartDTO = getCartDTO(authentication, cartCookie, response);
        }catch (CartNotFoundException exception){
            return new ResponseEntity<CartResponse>(new CartResponse(CartResult.GET_CART_LIST_FAILL, CartErrorResponse.of(exception.getErrorCode())),
                    HttpStatus.OK);
        }
        return new ResponseEntity<CartResponse>(new CartResponse(CartResult.GET_CART_LIST_SUCCESS, cartDTO),HttpStatus.OK);
    }

    /**
     * 장바구니를 조회하여 CartDTO 를 조회하는 메소드.
     * @param authentication
     * @param cartCookie 비회원 장바구니 쿠키 정보.
     * @return
     */
    private CartDTO getCartDTO(Authentication authentication, Cookie cartCookie,  HttpServletResponse response) throws CartNotFoundException{

        // 비회원 장바구니.
        if(authentication == null){
            if(cartCookie != null){
                String ckId = cartCookie.getValue();
                log.info("ckId: " + ckId);
                return cartService.getCookieCart(ckId);
            }else{
                log.info("cookie is null");
            }
        }
        // 회원 장바구니.
        else if(authentication != null){
            return cartService.getMemberCart(authentication.getName());
        }
        return null;
    }
}
