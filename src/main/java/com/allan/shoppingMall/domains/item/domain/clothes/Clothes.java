package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.domains.item.domain.Item;
import com.allan.shoppingMall.domains.item.domain.ItemImage;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Slf4j
public class Clothes extends Item {

    @Column(name = "eng_name")
    private String engName;

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ItemImage> itemImages = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ModelSize> modelSizes = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ClothesFabric> clothesFabrics = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ClothesDetail> clothesDetails = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ClothesSize> clothesSizes = new ArrayList<>();


    // 의류에 기타 정보를 담을 필드.
    @Column
    private String etc;

    @Builder
    public Clothes(String name, Long price, Long stockQuantity, String engName, String etc) {
        super(name, price, stockQuantity);
        this.engName = engName;
        this.etc = etc;
    }

    public void changeClothesFabrics(List<ClothesFabric> clothesFabrics){
        log.info("changeClothesFabrics() call");
        for(ClothesFabric clothesFabric : clothesFabrics){
            this.clothesFabrics.add(clothesFabric);
            clothesFabric.changeItem(this);
        }
    }

    public void changeClothesDetails(List<ClothesDetail> clothesDetails){
        log.info("changeClothesDetails() call");
        for(ClothesDetail clothesDetail : clothesDetails){
            this.clothesDetails.add(clothesDetail);
            clothesDetail.changeItem(this);
        }
    }

    public void changeClothesSizes(List<ClothesSize> clothesSizes){
        log.info("changeClothesSizes() call");
        for(ClothesSize clothesSize : clothesSizes){
            this.clothesSizes.add(clothesSize);
            clothesSize.changeItem(this);
        }
    }

    public void changeModelSizes(List<ModelSize> modelSizes){
        log.info("changeModelSizes() call");
        for(ModelSize modelSize : modelSizes){
            this.modelSizes.add(modelSize);
            modelSize.changeItem(this);
        }
    }
}
