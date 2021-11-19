package com.allan.shoppingMall.domains.item.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {

    PRODUCT("상품게시판", "상품 상세페이에서 사용 할 이미지"),
    PREVIEW("미리보기", "상품 목록 페이지에서 미리보기 이미지 또는 메인 이미지"),
    REVIEW("리뷰", "상품 후기용 이미지");

    private String whereToUse;
    private String desc;

}
