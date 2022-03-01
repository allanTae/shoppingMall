package com.allan.shoppingMall.domains.item.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * front 단에서 server단으로 전달하는 폼정보.
 * 서버측으로 전달 할 의류 상품 등록 정보 Object.
 */

@Getter
@Setter
public class ClothesForm {
    private String name;
    private String engName;
    private Long price;
    private Long stockQuantity;
    private int clothesColor;
    private Long categoryId;

    private List<ItemFabricDTO> clothesFabrics = new ArrayList<>();
    private List<ItemDetailDTO> clothesDetails = new ArrayList<>();
    private List<ItemSizeDTO> clothesSizes = new ArrayList<>();
    private List<ModelSizeDTO> modelSizes = new ArrayList<>();
    private List<MultipartFile> profileImageFiles = new ArrayList<>();
    private List<MultipartFile> detailImageFiles = new ArrayList<>();

    @Override
    public String toString(){
        return "ClothesRequest [name=" + this.name + ", engName=" + this.engName + ", price=" + this.price + ", stockQuantity=" + this.stockQuantity + ", clothesFabrics="
                + this.clothesFabrics + ", clothesDetails=" + this.clothesDetails + ", clothesSizes=" + this.clothesSizes + ", modelSize=" + this.modelSizes +
                ", categoryId= " + this.categoryId + "]";
    }
}
