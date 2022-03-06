package com.allan.shoppingMall.domains.item.domain.model;

import com.allan.shoppingMall.domains.item.domain.item.ImageType;
import com.allan.shoppingMall.domains.item.domain.item.ItemImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 프론트단에서 상품목록에서 사용 할 상품 정보를 담는 DTO 클래스.
 */
@Getter
@Setter
public class ItemSummaryDTO {
    private Long itemId;
    private String name;
    private Long price;
    private List<Long> profileImageIds; // 이미지 파일을 조회하기 위한 itemImage entity의 식별자.
    private String itemColor;
    private Long categoryId;

    @Builder
    public ItemSummaryDTO(Long itemId, String name, Long price, List<Long> profileImageIds, String itemColor, Long categoryId) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.profileImageIds = profileImageIds;
        this.itemColor = itemColor;
        this.categoryId = categoryId;
    }

    /**
     * itemImage Entity list를 profileImageId list로 변환하기 위한 메소드.
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
