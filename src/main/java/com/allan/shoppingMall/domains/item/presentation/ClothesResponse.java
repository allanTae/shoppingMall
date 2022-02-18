package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSummeryDTO;
import lombok.Getter;

/**
 * Clothes 도메인 rest api 정보를 저장하는 클래스입니다.
 */
@Getter
public class ClothesResponse {

    private String apiResultMessage; // 결과 메시지.
    private Boolean apiResult; // api 결과 값. 사용자단에서 결과를 확인하기 위해 사용합니다.
    private ClothesSummeryDTO clothesInfo; // api clothes 조회 정보를 저장하는 오브젝트.

    private ClothesErrorResponse clothesErrorResponse; // api 에러정보.

    /**
     * @param cartResult api 결과정보 오브젝트
     * @return
     */
    public ClothesResponse(ClothesResult cartResult){
        this.apiResultMessage = cartResult.getMessage();
        this.apiResult = cartResult.getResult();
    }

    /**
     * @param cartResult api 결과정보 오브젝트
     * @param clothesErrorResponse api 에러정보 오브젝트
     * @return
     */
    public ClothesResponse(ClothesResult cartResult, ClothesErrorResponse clothesErrorResponse){
        this.apiResultMessage = cartResult.getMessage();
        this.apiResult = cartResult.getResult();
        this.clothesErrorResponse = clothesErrorResponse;
    }

    /**
     * @param cartResult api 결과정보 오브젝트
     * @param clothesSummeryDTO api 의류 상품 요약정보 오브젝트.
     * @return
     */
    public ClothesResponse(ClothesResult cartResult, ClothesSummeryDTO clothesSummeryDTO){
        this.apiResultMessage = cartResult.getMessage();
        this.apiResult = cartResult.getResult();
        this.clothesErrorResponse = clothesErrorResponse;
        this.clothesInfo = clothesSummeryDTO;
    }
}
