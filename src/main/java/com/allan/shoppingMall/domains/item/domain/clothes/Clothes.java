package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.domains.item.domain.Color;
import com.allan.shoppingMall.domains.item.domain.Item;
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

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClothesFabric> clothesFabrics = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClothesDetail> clothesDetails = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClothesSize> clothesSizes = new ArrayList<>();


    // 의류에 기타 정보를 담을 필드.
    @Column
    private String etc;

    @Builder
    public Clothes(String name, Long price, Color color, String engName, String etc) {
        super(name, price, 0l,color);
        this.engName = engName;
        this.etc = etc;
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * ClothesFabric 등록함.
     * @param clothesFabrics
     */
    public void changeClothesFabrics(List<ClothesFabric> clothesFabrics){
        for(ClothesFabric clothesFabric : clothesFabrics){
            this.clothesFabrics.add(clothesFabric);
            clothesFabric.changeItem(this);
        }
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * ClothesDetail 등록함.
     * @param clothesDetails
     */
    public void changeClothesDetails(List<ClothesDetail> clothesDetails){
        for(ClothesDetail clothesDetail : clothesDetails){
            this.clothesDetails.add(clothesDetail);
            clothesDetail.changeItem(this);
        }
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * ClothesSize 등록함.
     * Clothes 상품의 경우, 사이즈별로 수량을 관리 해야 하기 때문에 ClothesSize에 모든 수량이 곧 clothes 상품의 재고량이 된다.
     * @param clothesSizes
     */
    public void changeClothesSizes(List<ClothesSize> clothesSizes){
        long totalQuantity = 0l;
        for(ClothesSize clothesSize : clothesSizes){
            totalQuantity += clothesSize.getStockQuantity();
            this.clothesSizes.add(clothesSize);
            clothesSize.changeItem(this);
        }
        addStockQuantity(totalQuantity); // 재고량을 증가하기 위한 메소드.
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

}
