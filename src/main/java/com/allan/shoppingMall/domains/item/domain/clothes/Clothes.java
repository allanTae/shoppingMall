package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.domains.item.domain.Item;
import com.allan.shoppingMall.domains.item.domain.ItemImage;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Clothes extends Item {

    @Column(name = "eng_name")
    private String engName;

    @OneToMany(mappedBy = "item")
    private List<ItemImage> itemImages = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<ModelSize> modelSizes = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<ClothesFabrics> clothesFabrics = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<ClothesDetails> clothesDetails = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<ClothesSizes> clothesSizes = new ArrayList<>();

    // 의류에 기타 정보를 담을 필드.
    @Column
    private String etc;

    @Builder
    public Clothes(String name, Long price, Long stockQuantity, String engName, String etc) {
        super(name, price, stockQuantity);
        this.engName = engName;
        this.etc = etc;
    }
}
