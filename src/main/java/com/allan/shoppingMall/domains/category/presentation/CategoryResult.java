package com.allan.shoppingMall.domains.category.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * category api 결과를 나타내는 enum 클래스 입니다.
 */
@AllArgsConstructor
@Getter
public enum CategoryResult {
    SAVE_CATEGORY_SUCCESS(true, "카테고리 생성에 성공하였습니다."),
    SAVE_CATEGORY_FAIL(false, "카테고리 생성에 실패하였습니다."),
    GET_CATEGORY_SUCCESS(true, "카테고리 조회에 성공하였습니다."),
    GET_CATEGORY_FAIL(false, "카테고리 조회에 실패하였습니다."),
    MODIFY_CATEGORY_SUCCESS(true, "카테고리 수정에 성공하였습니다."),
    MODIFY_CATEGORY_FAIL(false, "카테고리 수정에 실패하였습니다."),
    DELETE_CATEGORY_SUCCESS(true, "카테고리 삭제에 성공하였습니다."),
    DELETE_CATEGORY_FAIL(false, "카테고리 삭제에 실패하였습니다.");

    private Boolean result;
    private String message;
}
