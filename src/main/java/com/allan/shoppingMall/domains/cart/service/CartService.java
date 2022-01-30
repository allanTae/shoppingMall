package com.allan.shoppingMall.domains.cart.service;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.ItemNotFoundException;
import com.allan.shoppingMall.common.exception.MemberNotFoundException;
import com.allan.shoppingMall.common.exception.cart.CartNotFoundException;
import com.allan.shoppingMall.domains.cart.domain.Cart;
import com.allan.shoppingMall.domains.cart.domain.CartItem;
import com.allan.shoppingMall.domains.cart.domain.CartRepository;
import com.allan.shoppingMall.domains.cart.domain.model.CartDTO;
import com.allan.shoppingMall.domains.cart.domain.model.CartItemDTO;
import com.allan.shoppingMall.domains.cart.domain.model.CartRequest;
import com.allan.shoppingMall.domains.cart.domain.model.RequiredOption;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesRepository;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final ClothesRepository clothesRepository;

    /**
     * 회원 장바구니 생성 메소드.
     * @param cartRequest
     */
    @Transactional
    public void addMemberCart(CartRequest cartRequest, String authId){
        List<CartItem> cartItems = cartRequest.getCartItems().stream()
                .map(cartItemSummary -> {
                    Clothes clothes = clothesRepository.findById(cartItemSummary.getItemId()).orElseThrow(()
                            -> new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

                    return new CartItem(clothes, cartItemSummary.getCartQuantity(), cartItemSummary.getSize());
                }).collect(Collectors.toList());

        Member member = memberRepository.findByAuthIdLike(authId).orElseThrow(()
                -> new MemberNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        // 장바구니를 회원 가입시, 자동 생성하게 되면, orElse() 로직을 제거해도 된다.
        Cart cart = cartRepository.findByAuthId(authId)
                .orElse(Cart.builder()
                        .member(member)
                        .build());

        cart.addCartItems(cartItems);
        cartRepository.save(cart);
    }

    /**
     * 비회원 장바구니 생성 메소드.
     * @param cartRequest
     */
    @Transactional
    public void addTempCart(CartRequest cartRequest){
        List<CartItem> cartItems = cartRequest.getCartItems().stream()
                .map(cartItemSummary -> {
                    Clothes clothes = clothesRepository.findById(cartItemSummary.getItemId()).orElseThrow(()
                            -> new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

                    return new CartItem(clothes, cartItemSummary.getCartQuantity(), cartItemSummary.getSize());
                }).collect(Collectors.toList());

        Cart cart = Cart.builder()
                .ckId(cartRequest.getCartCkId())
                .build();

        cart.addCartItems(cartItems);
        cartRepository.save(cart);
    }

    /**
     * 비회원 장바구니에 추가하는 메소드(이미 비회원 장바구니가 존재하는 경우에 비회원 장바구니에 장바구니 상품을 추가하기 위한 메소드).
     * @param cartRequest 사용자가 전달 한 장바구니 정보.
     */
    @Transactional
    public void updatempCart(CartRequest cartRequest){
        Cart cart = cartRepository.findByCkId(cartRequest.getCartCkId()).orElseThrow(() -> new CartNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        List<CartItem> cartItems = cartRequest.getCartItems().stream()
                .map(cartItemSummary -> {
                    Clothes clothes = clothesRepository.findById(cartItemSummary.getItemId()).orElseThrow(()
                            -> new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

                    return new CartItem(clothes, cartItemSummary.getCartQuantity(), cartItemSummary.getSize());
                }).collect(Collectors.toList());

        cart.addCartItems(cartItems);
    }

    /**
     * 로그인시, 비회원 장바구니 상품을 회원 장바구니로 추가하는 메소드.
     * @param ckId 장바구니 쿠키 식별 아이디.
     * @param authId 로그인한 회원 아이디.
     */
    @Transactional
    public void updateMemberCartByTempCart(String ckId, String authId){
        try {
            Cart ckCart = cartRepository.findByCkId(ckId).orElseThrow(() -> new CartNotFoundException(ErrorCode.ENTITY_NOT_FOUND)); // 비회원 장바구니.
            Cart memberCart = cartRepository.findByAuthId(authId).get(); // 회원 장바구니.
            memberCart.addCartItems(ckCart.getCartItems()); // 비회원 장바구니 -> 회원 장바구니.
            cartRepository.delete(ckCart);
        }catch (CartNotFoundException e){

        }
    }

    /**
     * 회원 장바구니 DTO 를 반환하는 메소드 입니다.
     * @param authId 로그인한 회원 아이디
     * @return CartDTO
     */
    public CartDTO getMemberCart(String authId){
        CartDTO cartDTO = null;
        try {
            Cart cart = cartRepository.findByAuthId(authId).orElseThrow(()
                    -> new CartNotFoundException(ErrorCode.MEMBER_CART_NOT_FOUND));
            cartDTO = new CartDTO(cart.getCartId());

            for(CartItem cartItem : cart.getCartItems()){
                // cartDTO에 cartItemDTO가 존재하지 않는다면, cartItemDTO 를 추가.
                if(!cartDTO.getCartItems().contains(cartItem)){
                    cartDTO.getCartItems().add(CartItemDTO.builder()
                            .itemId(cartItem.getItem().getItemId())
                            .itemPrice(cartItem.getItem().getPrice())
                            .itemName(cartItem.getItem().getName())
                            .itemProfileImg(cartItem.getItem().getItemImages().get(0).getItemImageId())
                            .requiredOption(new RequiredOption(cartItem.getSize(), cartItem.getCartQuantity()))
                            .build());
                }else{
                    // 이미 cartDTO에 cartItemDTO가 존재한다면, 기존에 cartItemDTO 에 필수 정보만 추가.
                    int index = cartDTO.getCartItems().indexOf(cartItem);
                    cartDTO.getCartItems().get(index).addRequiredOption(cartItem.getSize(), cartItem.getCartQuantity());
                }
            }
        }catch (CartNotFoundException e){
            return null;
        }
        return cartDTO;
    }
}
