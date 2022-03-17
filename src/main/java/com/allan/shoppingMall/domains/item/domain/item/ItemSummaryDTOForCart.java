package com.allan.shoppingMall.domains.item.domain.item;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * 장바구니 정보 수정 modal 프론트단에서 사용할 의류 상품의 요약 정보.
 */
@Getter
@Setter
public class ItemSummaryDTOForCart {
    private Long itemId;
    private List<SizeLabel> sizes;
    private Long profileImgId;
    private String name;
    private Long price;

    @Builder
    public ItemSummaryDTOForCart(Long itemId, List<SizeLabel> sizes, Long profileImgId, String name, Long price) {
        this.itemId = itemId;
        this.sizes = sizes;
        this.profileImgId = profileImgId;
        this.name = name;
        this.price = price;
    }
}
