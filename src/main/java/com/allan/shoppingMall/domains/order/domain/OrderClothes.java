package com.allan.shoppingMall.domains.order.domain;

import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class OrderClothes extends OrderItem{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothes_size_id")
    private ClothesSize clothesSize;

    /**
     * 순서에 의존하는 생성자이기 떄문에 Builder 패턴을 사용하지 않는다.
     * @param orderQuantity
     * @param clothes
     * @param clothesSize 주문 받은 의상의 사이즈에 해당하는 clothesSize
     */
    public OrderClothes(Long orderQuantity, Clothes clothes, ClothesSize clothesSize) {
        super(orderQuantity, clothes);
        setClothesSize(clothesSize); // clothesSize stockQuantity 조정.
    }
    /**
     * order 시, 사이즈에 맞는 clothesSize 의 재고량을 줄이기 위한 메소드.
     * clothes 상품의 경우, 상품의 전체 재고량 = 사이즈별 재고량의 합이기 떄문에
     * Clothes 상품의 경우, 전체 재고량 뿐만 아니라 사이즈별 재고량 조절이 필요하다.
     * (전체 재고량의 경우, OrderClothes 의 부모인 OrderItem 의 생성자를 통해 조절이 된다.)
     * (OrderItem 의 setItem() 부분 참조.)
     */
    private void setClothesSize(ClothesSize clothesSize){
        clothesSize.subStockQuantity(this.getOrderQuantity());
        this.clothesSize = clothesSize;
    }

    /**
     * cancel order 시, clothesSize 의 재고량 과 전체 재고량을 원상복구 하기 위한 메소드.
     */
    public void cancleOrderClothes(){
        // 사이즈별 재고량 원상복구.
        this.clothesSize.addStockQuantity(this.getOrderQuantity());

        // 상품의 전체 재고량 원상복구.(OrderItem cancelOrderItem() 호출)
        cancelOrderItem();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getItem().getItemId() + this.getClothesSize().getSizeLabel().getId());
    }

    /**
     * orderClothes 경우, 동일한 itemId 를 가지고 있더라도 사이즈별로 주문 할 수 있기 때문에 주문 상품리스트에 동일한 itemId 를 가진 상품이 추가 될 수 있다.
     * 그렇기에 구별값을 itemId + sizeLabel 을 통해 구분 하도록 변경하였다.
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        log.info("OrderClothes equals() call!!!");
        if(obj == null){
            log.error("OrderClothes equals()'s parameter is null");
            return false;
        }
        if(this == obj)
            return true;
        if(!(obj instanceof OrderItem))
            return false;

        return this.getItem().getItemId() == ((OrderClothes) obj).getItem().getItemId() &&
                this.getClothesSize().getSizeLabel() == ((OrderClothes) obj).getClothesSize().getSizeLabel();
    }

}
