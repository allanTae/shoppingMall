package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSummeryDTO;
import com.allan.shoppingMall.domains.item.domain.item.ItemSummaryDTOForCart;
import com.allan.shoppingMall.domains.item.presentation.clothes.ClothesErrorResponse;
import com.allan.shoppingMall.domains.item.presentation.clothes.ClothesResult;
import lombok.Getter;

/**
 * item 도메인 rest api 정보를 저장하는 클래스입니다.
 */
@Getter
public class ItemResponse {

    private String apiResultMessage; // 결과 메시지.
    private Boolean apiResult; // api 결과 값. 사용자단에서 결과를 확인하기 위해 사용합니다.
    private ItemSummaryDTOForCart itemInfo; // api clothes 조회 정보를 저장하는 오브젝트.

    private ItemErrorResponse itemErrorResponse; // api 에러정보.

    /**
     * @param itemResult api 결과정보 오브젝트
     * @return
     */
    public ItemResponse(ItemResult itemResult){
        this.apiResultMessage = itemResult.getMessage();
        this.apiResult = itemResult.getResult();
    }

    /**
     * @param itemResult api 결과정보 오브젝트
     * @param itemErrorResponse api 에러정보 오브젝트
     * @return
     */
    public ItemResponse(ItemResult itemResult, ItemErrorResponse itemErrorResponse){
        this.apiResultMessage = itemResult.getMessage();
        this.apiResult = itemResult.getResult();
        this.itemErrorResponse = itemErrorResponse;
    }

    /**
     * @param itemResult api 결과정보 오브젝트
     * @param itemInfo api 의류 상품 요약정보 오브젝트.
     * @return
     */
    public ItemResponse(ItemResult itemResult, ItemSummaryDTOForCart itemInfo){
        this.apiResultMessage = itemResult.getMessage();
        this.apiResult = itemResult.getResult();
        this.itemInfo = itemInfo;
    }
}
