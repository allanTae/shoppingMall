package com.allan.shoppingMall.domains.cart.domain.model;

import com.allan.shoppingMall.domains.cart.domain.CartItem;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * 장바구니 상품 정보 클래스입니다.
 * (벡단에서 프론트단으로 장바구니 정보를 전달 하기 위해 사용합니다.)
 */
@Getter
@Setter
@Slf4j
public class CartItemDTO {

    private Long itemId;
    private Long itemProfileImg; // 장바구니 상품 프로필 이미지.
    private String itemName; // 장바구니 상품 이름.
    private List<RequiredOption> requiredOptions; // 필수정보.
    private Long itemPrice;
    private Long deliveryPrice; // 배송비.

    @Builder
    public CartItemDTO(Long itemId, Long itemProfileImg, String itemName, Long itemPrice, Long deliveryPrice, RequiredOption requiredOption) {
        this.itemId = itemId;
        this.itemProfileImg = itemProfileImg;
        this.itemName = itemName;
        setPrice(itemPrice);
        this.requiredOptions.add(requiredOption);
    }

    private void setPrice(Long itemPrice){
        this.itemPrice = itemPrice;
        if(itemPrice > 50000l)
            this.deliveryPrice = 0l;
        else{
            this.deliveryPrice = 3000l;
        }
    }

    @Override
    public String toString() {
        return "CartItemDTO{" +
                "itemProfileImg=" + itemProfileImg +
                ", itemName='" + itemName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        log.info("CartItemDTO equals() call!!!");
        if(obj == null){
            log.error("CartItemDTO equals()'s parameter is null");
            return false;
        }
        if(this == obj)
            return true;
        if(!(obj instanceof CartItem))
            return false;

        CartItem inputCartItem = (CartItem) obj;

        if(this.itemId == inputCartItem.getCartItemId()){
            // 필수정보만 추가.
            addRequiredOption(inputCartItem.getSize(), inputCartItem.getCartQuantity());

            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.itemId);
    }

    /**
     * 필수정보를 추가하는 메소드.
     * (이미 CartDTO 에 존재하는 CartItemDTO 경우, 주문에 필요한 사이즈, 수량 정보의 필수정보만 추가하는 메소드이다.)
     */
    public void addRequiredOption(SizeLabel size, Long cartQuantity){
        RequiredOption requiredOption = new RequiredOption(size, cartQuantity);
        this.requiredOptions.add(requiredOption);
    }
}
