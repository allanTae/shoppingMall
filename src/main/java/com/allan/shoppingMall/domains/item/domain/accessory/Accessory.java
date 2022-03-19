package com.allan.shoppingMall.domains.item.domain.accessory;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.item.ItemSizeModifyFailException;
import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.item.Color;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Accessory extends Item {

    @Column(name = "eng_name", nullable = false)
    private String engName;

    @Builder
    public Accessory(String name, Long price, Color color, String engName){
        super(name, price, color);
        this.engName = engName;
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * AccessorySize 등록함.
     * @param accessorySizes
     */
    public void changeAccessorySize(List<AccessorySize> accessorySizes){
        long totalQuantity = 0l;
        for(AccessorySize accessorySize : accessorySizes){
            totalQuantity += accessorySize.getStockQuantity();
            this.getItemSizes().add(accessorySize);
            accessorySize.changeItem(this);
        }
        addStockQuantity(totalQuantity); // 재고량을 증가하기 위한 메소드.
    }

    public void changeEngName(String engName){
        this.engName = engName;
    }

    /**
     * ClothesSize list 정보로 사이즈 도메인을 수정하는 메소드입니다.
     *
     * @param accessorySizes
     */
    public void updateAccessorySize(List<AccessorySize> accessorySizes){
        for(AccessorySize accessorySize : accessorySizes){
            // 존재하면,
            if(this.getItemSizes().contains(accessorySize)){
                int index = this.getItemSizes().indexOf(accessorySize);

                Long accessorySizeQuantity = accessorySize.getStockQuantity(); // 변경 해야 하는 clothesSize quantity.
                AccessorySize findAccessorySize = null;

                if(this.getItemSizes().get(index) instanceof  AccessorySize)
                    findAccessorySize = (AccessorySize) this.getItemSizes().get(index);
                else
                    throw new ItemSizeModifyFailException("변경 할 수 없는 AccessorySize 입니다.", ErrorCode.INTERNAL_SERVER_ERROR);

                // 사이즈 도메인 재고량 변경.
                    // 변경 수량이 0 이하면 예외가 발생.
                    if (accessorySizeQuantity < 0) {
                        throw new ItemSizeModifyFailException(ErrorCode.ITEM_SIZE_QUANTITY_NOT_ENOUGH);
                    }
                    // 변경 수량이 0 인경우, 삭제.
                    if (accessorySizeQuantity == 0) {
                        this.getItemSizes().remove(findAccessorySize);
                    }else{
                        // 변경 수량이 기존 수량보다 많은경우, 재고량 증가.
                        if(accessorySizeQuantity - findAccessorySize.getStockQuantity() > 0){
                            Long addStockQuantity = accessorySizeQuantity -findAccessorySize.getStockQuantity();

                            // 상품 사이즈 재고량 변경.
                            findAccessorySize.addStockQuantity(addStockQuantity);
                            // 상품 총 재고량 변경.
                            this.addStockQuantity(addStockQuantity);

                        // 변경 수량이 기존 수량보다 적은경우, 재고량 감소.
                        }else if(accessorySizeQuantity - findAccessorySize.getStockQuantity() < 0){
                            Long subStockQunaity = findAccessorySize.getStockQuantity() - accessorySizeQuantity;

                            // 상품 사이즈 재고량 변경.
                            findAccessorySize.subStockQuantity(subStockQunaity);
                            // 상품 총 재고량 변경.
                            this.subtractStockQuantity(subStockQunaity);
                        }

                        // 사이즈 도메인 재고량 외 정보 수정.
                        findAccessorySize.updateAccessorySizeInfo(accessorySize.getSizeLabel(), accessorySize.getWidthLength(), accessorySize.getHeightLength());
                    }

                // 없으면,
            }else{
                this.getItemSizes().add(accessorySize);
                accessorySize.changeItem(this);

                // 상품 총 재고량 추가.
                addStockQuantity(accessorySize.getStockQuantity());
            }
        }
    }
}
