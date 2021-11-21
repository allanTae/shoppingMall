package com.allan.shoppingMall.domains.item.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 서버측으로 전달 할 의류 상품 등록 정보 Object.
 */

@Getter
@Setter
public class ClothesForm {
    private String name;
    private String engName;
    private Long price;
    private Long stockQuantity;

    private List<ClothesFabricsDTO> clothesFabrics = new ArrayList<>();
    private List<ClothesDetailsDTO> clothesDetails = new ArrayList<>();
    private List<ClothesSizesDTO> clothesSizes = new ArrayList<>();
    private List<ModelSizeDTO> modelSizes = new ArrayList<>();

    @Override
    public String toString(){
        return "ClothesRequest [name=" + this.name + ", engName=" + this.engName + ", price=" + this.price + ", stockQuantity=" + this.stockQuantity + ", clothesFabrics="
                + this.clothesFabrics + ", clothesDetails=" + this.clothesDetails + ", clothesSizes=" + this.clothesSizes + ", modelSize=" + this.modelSizes + "]";
    }
}
