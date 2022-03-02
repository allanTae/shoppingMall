package com.allan.shoppingMall.domains.cart.domain;

import com.allan.shoppingMall.common.domain.BaseTimeEntity;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.cart.CartAddItemFailException;
import com.allan.shoppingMall.domains.item.domain.item.ItemSize;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Objects;

/**
 * 장바구니 상품 정보들을 담고 있는 도메인.
 * 장바구니 상품들은 비회원도 추가 해야 하기 때문에 작성자, 수정자 정보를 JpaAuditing 으로 처리하는데 무리가 있기에
 * BaseEntity 가 아닌, BaseTimeEntity를 상속하도록 함.
 */
@Entity
@Table(name = "cart_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class CartItem extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private Long cartQuantity;

    @Column(name = "item_size")
    @Enumerated(value = EnumType.STRING)
    private SizeLabel size;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * CartItem 엔티티의 경우 초기 가입시 로그인 된 아이디를 사용하여 생성자와 수정자를 설정하는
     * JpaAuditing LoginIdAuditorAware 를 사용하는 불가능하기 때문에
     * 엔티티 최초 persist 시점에 생성하여 입력하도록 한다.
     */
    @PrePersist
    public void setUp(){
        this.createdBy = "system";
        this.updatedBy = "system";
    }

    /**
     * clothes 상품을 추가 할 때 사용 할 생성자 메소드.
     * @param item
     * @param cartQuantity
     * @param size
     */
    @Builder
    public CartItem(Item item, Long cartQuantity, SizeLabel size) {
        this.item = item;
        this.cartQuantity =cartQuantity;
        this.size = size;
    }

    /**
     * 장바구니에서 재고량을 체크 하여 장바구니에서 수량을 추가하는 메소드.(나중에 반영 할 예정)
     * @param cartQuantity
     * @param clothesSize
     */
    private void setQuantity(Long cartQuantity, ItemSize clothesSize){
        if(clothesSize.getStockQuantity() < cartQuantity)
            throw new CartAddItemFailException(ErrorCode.ITEM_STOCK_QUANTITY_EXCEEDED);
        else
            this.cartQuantity = cartQuantity;
    }

    /**
     * cart 정보를 수정하기 위한 메소드.
     * @param cart
     */
    public void changeCart(Cart cart){
        this.cart = cart;
    }

    /**
     * 장바구니 들어간 상품에 수량을 증가하는 메소드.
     * @param cartQuantity
     */
    public void addCartQuantity(Long cartQuantity){
        this.cartQuantity += cartQuantity;
    }

    /**
     * 장바구니에 들어간 상품에 수량을 감소하는 메소드.
     * @param cartQuantity
     */
    public void subCartQuantity(Long cartQuantity){
        if(this.cartQuantity - cartQuantity < 0){
            log.error("0 보다 작은 수량은 담을 수 없습니다.");
            throw new CartAddItemFailException(ErrorCode.INPUT_CART_ITEM_QUANTITY_LESS_THAN_MINIMUM_VALUE);
        }else{
            this.cartQuantity -= cartQuantity;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.item.getItemId());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            log.error("CartItem equals()'s parameter is null");
            return false;
        }
        if(this == obj)
            return true;
        if(!(obj instanceof CartItem))
            return false;

        CartItem inputCartItem = (CartItem) obj;

        return this.item.getItemId() == inputCartItem.getItem().getItemId() &&
                this.size.getDesc().equals(inputCartItem.size.getDesc());
    }
}
