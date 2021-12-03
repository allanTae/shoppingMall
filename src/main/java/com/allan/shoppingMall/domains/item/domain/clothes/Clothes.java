package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.domains.item.domain.Color;
import com.allan.shoppingMall.domains.item.domain.Item;
import com.allan.shoppingMall.domains.item.domain.ItemImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    public Clothes(String name, Long price, Long stockQuantity, Color color, String engName, String etc) {
        super(name, price, stockQuantity,color);
        this.engName = engName;
        this.etc = etc;
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * ClothesFabric 등록함.
     * @param clothesFabrics
     */
    public void changeClothesFabrics(List<ClothesFabric> clothesFabrics){
        log.info("Clothes Entity's changeClothesFabrics() call");
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
        log.info("Clothes Entity's changeClothesDetails() call");
        for(ClothesDetail clothesDetail : clothesDetails){
            this.clothesDetails.add(clothesDetail);
            clothesDetail.changeItem(this);
        }
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * ClothesSize 등록함.
     * @param clothesSizes
     */
    public void changeClothesSizes(List<ClothesSize> clothesSizes){
        log.info("Clothes Entity's changeClothesSizes() call");
        for(ClothesSize clothesSize : clothesSizes){
            this.clothesSizes.add(clothesSize);
            clothesSize.changeItem(this);
        }
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * ModelSize 등록함.
     * @param modelSizes
     */
    public void changeModelSizes(List<ModelSize> modelSizes){
        log.info("Clothes Entity's changeModelSizes() call");
        for(ModelSize modelSize : modelSizes){
            this.modelSizes.add(modelSize);
            modelSize.changeItem(this);
        }
    }

}
