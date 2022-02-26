package com.allan.shoppingMall.domains.category.presentation;

import com.allan.shoppingMall.domains.category.domain.model.CategoryDTO;
import lombok.Getter;

/**
 * server category api response
 * category rest api 응답 클래스입니다.
 */
@Getter
public class CategoryResponse {
    private String apiResultMessage; // 결과 메시지.
    private Boolean apiResult; // api 결과 값. 사용자단에서 결과를 확인하기 위해 사용합니다.

    private CategoryDTO category;

    private CategoryErrorResponse categoryErrorResponse;

    /**
     * @param apiResultMessage api 메시지.
     * @param apiResult api 결과.
     * @param categoryErrorResponse 에러 응답 정보.
     * @return
     */
    public CategoryResponse(String apiResultMessage, Boolean apiResult, CategoryErrorResponse categoryErrorResponse) {
        this.apiResultMessage = apiResultMessage;
        this.apiResult = apiResult;
        this.categoryErrorResponse = categoryErrorResponse;
    }

    /**
     * cartInfo 를 제외한 CartResponse 생성자.
     * @param categoryResult api 결과정보 오브젝트
     */
    public CategoryResponse(CategoryResult categoryResult){
        this.apiResultMessage = categoryResult.getMessage();
        this.apiResult = categoryResult.getResult();
    }

    public CategoryResponse(CategoryResult categoryResult, CategoryDTO categoryDTO){
        this.apiResultMessage = categoryResult.getMessage();
        this.apiResult = categoryResult.getResult();
        this.category = categoryDTO;
    }

    /**
     * @param categoryResult api 결과정보 오브젝트
     * @param categoryErrorResponse api 에러정보 오브젝트
     * @return
     */
    public CategoryResponse(CategoryResult categoryResult, CategoryErrorResponse categoryErrorResponse){
        this.apiResultMessage = categoryResult.getMessage();
        this.apiResult = categoryResult.getResult();
        this.categoryErrorResponse = categoryErrorResponse;
    }
}
