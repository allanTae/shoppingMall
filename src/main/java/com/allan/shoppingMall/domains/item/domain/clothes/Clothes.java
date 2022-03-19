package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.item.ItemSizeModifyFailException;
import com.allan.shoppingMall.domains.item.domain.item.Color;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * clothes 상품은 '의상' 종류를 나타내는 상품으로 기존의 상품들과 다른 점은
 * 재고량을 관리하는 방식이 다르다. 의상은 사이즈별로 재고량이 상이 할 수 있기 떄문에,
 * 사이즈별 정보를 담당하는 ClothesSize 엔티티에서 각 사이즈별 재고량의 정보를 관리하고,
 * 의상 총재고량 = 사이즈별 재고량의 합 이 된다.
 *
 * 테스트시에 재고량을 조절하기 위해선 ClothesSize 정보가 필요하며, changeClothesSizes() 메소드를 호출함으로써
 * 재고량을 조절 할 수 있습니다.
 */
@Entity
@Getter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clothes extends Item {

    @Column(name = "eng_name", nullable = false)
    private String engName;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ModelSize> modelSizes = new ArrayList<>();

    // 의류에 기타 정보를 담을 필드.
    @Column
    private String etc;

    @Builder
    public Clothes(String name, Long price, Color color, String engName, String etc) {
        super(name, price, color);
        this.engName = engName;
        this.etc = etc;
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * ModelSize 등록함.
     * @param modelSizes
     */
    public void changeModelSizes(List<ModelSize> modelSizes){
        for(ModelSize modelSize : modelSizes){
            this.modelSizes.add(modelSize);
            modelSize.changeItem(this);
        }
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * ClothesSize 등록함.
     * @param clothesSizes
     */
    public void changeClothesSize(List<ClothesSize> clothesSizes){
        long totalQuantity = 0l;
        for(ClothesSize clothesSize : clothesSizes){
            totalQuantity += clothesSize.getStockQuantity();
            this.getItemSizes().add(clothesSize);
            clothesSize.changeItem(this);
        }
        addStockQuantity(totalQuantity); // 재고량을 증가하기 위한 메소드.

    }

    public void changeEngName(String engName){
        this.engName = engName;
    }

    /**
     * ClothesSize list 정보로 사이즈 도메인을 수정하는 메소드입니다.
     *
     * @param clothesSizes
     */
    public void updateClothesSize(List<ClothesSize> clothesSizes){
        for(ClothesSize clothesSize : clothesSizes){
            // 존재하면,
            if(this.getItemSizes().contains(clothesSize)){
                int index = this.getItemSizes().indexOf(clothesSize);

                Long clothesSizeQuantity = clothesSize.getStockQuantity(); // 변경 해야 하는 clothesSize quantity.
                ClothesSize findClothesSize = null;

                if(this.getItemSizes().get(index) instanceof  ClothesSize)
                    findClothesSize = (ClothesSize) this.getItemSizes().get(index);
                else
                    throw new ItemSizeModifyFailException("변경 할 수 없는 ClothesSize 입니다.", ErrorCode.INTERNAL_SERVER_ERROR);

                // 사이즈 도메인 재고량 변경.
                    // 변경 수량이 0 이하면 예외가 발생.
                    if (clothesSizeQuantity < 0) {
                        throw new ItemSizeModifyFailException(ErrorCode.ITEM_SIZE_QUANTITY_NOT_ENOUGH);
                    }
                    // 변경 수량이 0 인경우, 삭제.
                    if (clothesSizeQuantity == 0) {
                        this.getItemSizes().remove(findClothesSize);
                    }else{
                        // 변경 수량이 기존 수량보다 많은경우, 재고량 증가.
                        if(clothesSizeQuantity - findClothesSize.getStockQuantity() > 0){
                            Long addStockQuantity = clothesSizeQuantity -findClothesSize.getStockQuantity();

                            // 상품 사이즈 재고량 변경.
                            findClothesSize.addStockQuantity(addStockQuantity);
                            // 상품 총 재고량 변경.
                            this.addStockQuantity(addStockQuantity);

                        // 변경 수량이 기존 수량보다 적은경우, 재고량 감소.
                        }else if(clothesSizeQuantity - findClothesSize.getStockQuantity() < 0){
                            Long subStockQunaity = findClothesSize.getStockQuantity() - clothesSizeQuantity;

                            // 상품 사이즈 재고량 변경.
                            findClothesSize.subStockQuantity(subStockQunaity);
                            // 상품 총 재고량 변경.
                            this.subtractStockQuantity(subStockQunaity);
                        }

                        // 사이즈 도메인 재고량 외 정보 수정.
                        findClothesSize.updateClothesSizeInfo(clothesSize.getSizeLabel(), clothesSize.getBackLength(), clothesSize.getChestWidth(),
                                clothesSize.getShoulderWidth(), clothesSize.getSleeveLength(), clothesSize.getWaistWidth(), clothesSize.getHeapWidth(), clothesSize.getBottomWidth());

                    }

            // 없으면,
            }else{
                this.getItemSizes().add(clothesSize);
                clothesSize.changeItem(this);
                // 상품 총 재고량 추가.
                addStockQuantity(clothesSize.getStockQuantity());
            }
        }
    }

}
