package com.allan.shoppingMall.domains.item.domain.model;

import com.allan.shoppingMall.domains.item.domain.item.ImageType;
import com.allan.shoppingMall.domains.item.domain.item.ItemImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 프론트단으로 전달 할 의상 요약정보를 가지고 있는 DTO 클래스.
 */
@Getter
@Setter
public class ClothesSummaryDTOForIndex {

    private Long clothesId;
    private String clothesName;
    private Long price;
    private List<Long> profileImageIds; // 이미지 파일을 조회하기 위한 itemImage entity의 식별자.
    private String imagePath;
    private ImageType imageType;
    private String clothesColor;
    private Long categoryId;

    @Builder
    public ClothesSummaryDTOForIndex(Long clothesId, String clothesName, Long price, List<Long> profileImageIds, String clothesColor, Long categoryId) {
        this.clothesId = clothesId;
        this.clothesName = clothesName;
        this.price = price;
        this.profileImageIds = profileImageIds;
        this.clothesColor = clothesColor;
        this.categoryId = categoryId;
    }

    /**
     * itemImage Entity list를 ClothesSummaryDTO list로 변환하기 위한 메소드.
     * @param  itemImages
     * @return List<String>
     */
    public static List<Long> toImagePath(List<ItemImage> itemImages){
        return itemImages
                .stream()
                .filter(itemImage -> itemImage.getImageType().getWhereToUse().equals(ImageType.PREVIEW.getWhereToUse()))
                .map(itemImage -> {
                    return itemImage.getItemImageId();
                })
                .collect(Collectors.toList());
    }

}
