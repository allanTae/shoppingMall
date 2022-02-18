package com.allan.shoppingMall.domains.cart.domain;

import com.allan.shoppingMall.common.domain.BaseTimeEntity;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.cart.CartModifyFailException;
import com.allan.shoppingMall.domains.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 쇼핑몰 장바구니 도메인.
 * 장바구니 도메인은 비회원도 추가 해야 하기 때문에 작성자, 수정자 정보를 JpaAuditing 으로 처리하는데 무리가 있기에
 * BaseEntity 가 아닌, BaseTimeEntity를 상속하도록 함.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Cart extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", unique = true)
    private Member member;

    // 비회원이 장바구니 생성시, 장바구니와 쿠키정보 연관관계를 위한 필드.
    @Column(name = "cookie_id", unique = true)
    private String ckId;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<CartItem>();

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * Cart 엔티티의 경우 초기 가입시 로그인 된 아이디를 사용하여 생성자와 수정자를 설정하는
     * JpaAuditing LoginIdAuditorAware 를 사용하는 불가능하기 때문에
     * 엔티티 최초 persist 시점에 생성하여 입력하도록 한다.
     */
    @PrePersist
    public void setUp(){
        this.createdBy = "system";
        this.updatedBy = "system";
    }

    /**
     * 장바구니에 상품을 추가하는 메소드입니다.
     * 이미 장바구니에 추가 된 상품인 경우는 상품의 수량만 추가합니다.
     * @param cartItems 장바구니 상품
     */
    public void addCartItems(List<CartItem> cartItems){
        for(CartItem cartItem : cartItems){
            if(this.cartItems.contains(cartItem)){
                int cartItemIndex = this.cartItems.indexOf(cartItem);
                this.cartItems.get(cartItemIndex).addCartQuantity(cartItem.getCartQuantity());
            }else{
                cartItem.changeCart(this);
                this.cartItems.add(cartItem);
            }

        }
    }

    /**
     * 장바구니에 상품을 제거하는 메소드입니다.
     * @param cartItems 장바구니 상품
     */
    public void substractCartItems(List<CartItem> cartItems){
        for(CartItem cartItem : cartItems){
            if(this.cartItems.contains(cartItem)){
                this.cartItems.remove(cartItem);
            }
        }
    }

    /**
     * 장바구니 상품을 수정하는 메소드입니다.
     * @param cartItems 장바구니 상품 리스트.
     * @return
     */
    public void modifyCartItems(List<CartItem> cartItems){
        for(CartItem cartItem : cartItems){
            // 있으면 변경.
            if(this.cartItems.contains(cartItem)) {
                log.info("이미 존재하는 옵션>>.");
                log.info("itemId: " + cartItem.getItem().getItemId());
                log.info("cartQuantity: " + cartItem.getCartQuantity());
                log.info("size: " + cartItem.getSize().getDesc());
                log.info("이미 >>");

                int index = this.cartItems.indexOf(cartItem);

                Long cartItemQuantityRequest = cartItem.getCartQuantity(); // 변경 요청 한 장바구니 상품 수량.
                CartItem findCartItem = this.cartItems.get(index);

                if (cartItemQuantityRequest < 0) {
                    throw new CartModifyFailException(ErrorCode.INPUT_CART_ITEM_QUANTITY_LESS_THAN_MINIMUM_VALUE);
                }
                // 변경 수량이 0 인경우, 삭제.
                if (cartItemQuantityRequest == 0) {
                    this.cartItems.remove(findCartItem);
                }
                // 변경 수량이 기존 수량보다 많은경우, 증가.
                else if(cartItemQuantityRequest - findCartItem.getCartQuantity() > 0){
                    findCartItem.addCartQuantity(cartItemQuantityRequest - findCartItem.getCartQuantity());
                // 변경 수량이 기존 수량보다 적은경우, 감소.
                }else if(cartItemQuantityRequest - findCartItem.getCartQuantity() < 0){
                    findCartItem.subCartQuantity(findCartItem.getCartQuantity() - cartItemQuantityRequest);
                }
            }else{
                // 없으면 추가.
                log.info("new 옵션>>.");
                log.info("itemId: " + cartItem.getItem().getItemId());
                log.info("cartQuantity: " + cartItem.getCartQuantity());
                log.info("size: " + cartItem.getSize().getDesc());
                log.info("new >>");
                cartItem.changeCart(this);
                this.cartItems.add(cartItem);
            }
        }
    }

    @Builder
    public Cart(Member member, String ckId) {
        this.member = member;
        this.ckId = ckId;
    }
}
