package com.allan.shoppingMall.domains.item.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * clothes api 결과를 나타내는 enum 클래스 입니다.
 */
@Getter
@AllArgsConstructor
public enum ClothesResult {

    GET_CLOTHES_INFO_SUCCESS(true, "상품 조회에 성공하였습니다."),
    GET_CLOTHES_INFO_FAIL(false, "상품 조회에 실패하였습니다.");

    private Boolean result;
    private String message;
}
