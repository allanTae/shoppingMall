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
import com.allan.shoppingMall.domains.item.domain.Item;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesRepository;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import com.allan.shoppingMall.domains.order.domain.model.OrderItemSummaryRequest;
import com.allan.shoppingMall.domains.order.domain.model.OrderLineRequest;
import com.allan.shoppingMall.domains.order.domain.model.OrderSummaryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        // 장바구니를 회원 가입시, 자동 생성하게 되면, orElse() 로직을 제거해도 된다.
        Cart cart = cartRepository.findByAuthId(authId)
                .orElseThrow(() -> new CartNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        List<CartItem> cartItems = cartRequest.getCartItems().stream()
                .map(cartItemSummary -> {
                    Clothes clothes = clothesRepository.findById(cartItemSummary.getItemId()).orElseThrow(()
                            -> new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
                    log.info("cartItemSummary.quantity: " + cartItemSummary.getCartQuantity());
                    log.info("cartItemSummary.size: " + cartItemSummary.getSize().getDesc());
                    return new CartItem(clothes, cartItemSummary.getCartQuantity(), cartItemSummary.getSize());
                }).collect(Collectors.toList());

        cart.addCartItems(cartItems);
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
     * 장바구니가 존재하지 않는 경우 null 을 반환 합니다.
     * @param authId 로그인한 회원 아이디
     * @return CartDTO or null
     */
    public CartDTO getMemberCart(String authId){
        CartDTO cartDTO = null;
        try {
            Cart cart = cartRepository.findByAuthId(authId).orElseThrow(()
                    -> new CartNotFoundException(ErrorCode.MEMBER_CART_NOT_FOUND));
            cartDTO = transferDTO(cart);
        }catch (CartNotFoundException e){
            log.info(e.getMessage());
        }finally {
            return cartDTO;
        }
    }

    /**
     * 비회원 장바구니 DTO 를 반환하는 메소드입니다.
     * 장바구니가 존재하지 않는 경우 null 을 반환 합니다.
     * @param ckId 쿠키 아이디(장바구니 조회 목적).
     * @return CartDTO
     */
    public CartDTO getCookieCart(String ckId){
        CartDTO cartDTO = null;
        try{
            Cart cart = cartRepository.findByCkId(ckId).orElseThrow(() ->
                    new CartNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
            cartDTO = transferDTO(cart);
            cartDTO.setCkId(ckId);
        }catch (CartNotFoundException e){
            log.info(e.getMessage());
        }finally {
            return cartDTO;
        }
    }

    private CartDTO transferDTO(Cart cart){
        CartDTO cartDTO = new CartDTO(cart.getCartId());

        for(CartItem cartItem : cart.getCartItems()) {
            Item item = cartItem.getItem(); // 장바구니 상품 정보.
            // 이미 장바구니에 같은 상품이 존재하는 경우.
            if (cartDTO.getCartItems().containsKey(item.getItemId())) {
                cartDTO.getCartItems()
                        .get(item.getItemId())
                        .getRequiredOptions()
                        .add(new RequiredOption(cartItem.getCartQuantity(),
                                cartItem.getSize()));
            }
            // 장바구니에 같은 상품이 존재하지 않는 경우.
            else {
                cartDTO.getCartItems().put(item.getItemId(),
                        new CartItemDTO(item.getItemId(), item.getItemImages().get(0).getItemImageId(), item.getName(), item.getPrice(), cartItem.getCartQuantity(), cartItem.getSize()));
            }
        }

        return cartDTO;
    }

    /**
     * 조회한 장바구니DTO 내에 하나의 상품만 주문 요약정보로 변환하는 함수.
     * @param cartDTO
     * @param itemId
     * @return
     */
    public OrderSummaryRequest transferOrderSummary(CartDTO cartDTO, Long itemId){
        CartItemDTO cartItemDTO = cartDTO.getCartItems().get(itemId);

        Long totalQuantity = 0l;
        Long totalAmount = 0l;
        List<OrderItemSummaryRequest> orderItemSummaryRequestList = new ArrayList<>();

        for(int i=0; i < cartItemDTO.getRequiredOptions().size(); i++){
            totalAmount += cartItemDTO.getRequiredOptions().get(i).getCartItemQuantity() * cartItemDTO.getItemPrice();
            totalQuantity += cartItemDTO.getRequiredOptions().get(i).getCartItemQuantity();

            orderItemSummaryRequestList.add(OrderItemSummaryRequest.builder()
                    .itemId(cartItemDTO.getItemId())
                    .itemName(cartItemDTO.getItemName())
                    .orderQunatity(cartItemDTO.getRequiredOptions().get(i).getCartItemQuantity())
                    .previewImg(cartItemDTO.getItemProfileImg())
                    .price(cartItemDTO.getItemPrice())
                    .size(cartItemDTO.getRequiredOptions().get(i).getItemSize())
                    .build());
        }

        return new OrderSummaryRequest(totalAmount, totalQuantity, orderItemSummaryRequestList);
    }

}
