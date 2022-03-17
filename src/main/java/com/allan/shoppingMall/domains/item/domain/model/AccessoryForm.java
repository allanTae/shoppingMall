package com.allan.shoppingMall.domains.item.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * front 단에서 server단으로 전달하는 폼정보.
 * 서버측으로 전달 할 악세서리 상품 등록 정보 Object.
 */
@Getter
@Setter
@NoArgsConstructor
public class AccessoryForm {
    private String name;
    private String engName;
    private Long price;
    private int accessoryColor;
    private Long categoryId;

    // 수정폼에서 전달 할 의류 상품 도메인 아이디.
    private Long accessoryId;
    private String mode;

    private List<ItemFabricDTO> itemFabrics = new ArrayList<>();
    private List<ItemDetailDTO> itemDetails = new ArrayList<>();
    private List<AccessorySizeDTO> accessorySizes = new ArrayList<>();
    private List<MultipartFile> profileImageFiles = new ArrayList<>();
    private List<MultipartFile> detailImageFiles = new ArrayList<>();

    @Builder
    public AccessoryForm(String name, String engName, Long price, int accessoryColor, Long categoryId, Long accessoryId, String mode, List<ItemFabricDTO> itemFabrics, List<ItemDetailDTO> itemDetails, List<AccessorySizeDTO> accessorySizes, List<MultipartFile> profileImageFiles, List<MultipartFile> detailImageFiles) {
        this.name = name;
        this.engName = engName;
        this.price = price;
        this.accessoryColor = accessoryColor;
        this.categoryId = categoryId;
        this.accessoryId = accessoryId;
        this.mode = mode;
        this.itemFabrics = itemFabrics;
        this.itemDetails = itemDetails;
        this.accessorySizes = accessorySizes;
        this.profileImageFiles = profileImageFiles;
        this.detailImageFiles = detailImageFiles;
    }

    @Override
    public String toString() {
        return "AccessoryForm{" +
                "name='" + name + '\'' +
                ", engName='" + engName + '\'' +
                ", price=" + price +
                ", accessoryColor=" + accessoryColor +
                ", categoryId=" + categoryId +
                ", accessoryId=" + accessoryId +
                ", mode='" + mode + '\'' +
                ", itemFabrics=" + itemFabrics +
                ", itemDetails=" + itemDetails +
                ", accessorySizes=" + accessorySizes +
                '}';
    }
}
