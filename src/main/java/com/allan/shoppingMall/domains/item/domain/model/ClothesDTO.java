package com.allan.shoppingMall.domains.item.domain.model;

import com.allan.shoppingMall.domains.item.domain.ItemImage;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesDetail;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesFabric;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.clothes.ModelSize;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClothesDTO {

    private Long clothesId;

    private String name;

    private Long price;

    private Long stockQuantity;

    private String engName;

    private List<String> profileClothesImages = new ArrayList<>();

    private List<ModelSize> modelSizes = new ArrayList<>();

    private List<ClothesFabric> clothesFabrics = new ArrayList<>();

    private List<ClothesDetail> clothesDetails = new ArrayList<>();

    private List<ClothesSize> clothesSizes = new ArrayList<>();

    // 의류에 기타 정보를 담을 필드.
    private String etc;

    /**
     * itemRepository의 getClothesList() 를 통해 DTO List 를 반환 할 때 사용.
     */
    public ClothesDTO(Long clothesId, String name, Long price, List<String> profileClothesImages) {
        this.clothesId = clothesId;
        this.name = name;
        this.price = price;
        this.profileClothesImages = profileClothesImages;
    }
}
