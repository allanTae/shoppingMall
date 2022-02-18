package com.allan.shoppingMall.domains.item.domain.clothes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 장바구니 정보 수정 modal 프론트단에서 사용할 의류 상품의 요약 정보.
 */
@Getter
@Setter
public class ClothesSummeryDTO {
    private Long clothesId;
    private List<SizeLabel> sizes;
    private Long profileImgId;
    private String clothesName;
    private Long clothesPrice;

    @Builder
    public ClothesSummeryDTO(Long clothesId, List<SizeLabel> sizes, Long profileImgId, String clothesName, Long clothesPrice) {
        this.clothesId = clothesId;
        this.sizes = sizes;
        this.profileImgId = profileImgId;
        this.clothesName = clothesName;
        this.clothesPrice = clothesPrice;
    }
}
