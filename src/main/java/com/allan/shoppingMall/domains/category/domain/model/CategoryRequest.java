package com.allan.shoppingMall.domains.category.domain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 프론트단에서 벡단으로 카테고리 정보를 전달하기 위한 사용하는 클래스.
 */
@Getter
@Setter
public class CategoryRequest {

    private String name; // 카테고리 이름.
    private String branch; // 카테고리 그룹.
    private String parentCategoryName; // 부모 카테고리 이름.
    private int categoryCode; // 상품 도메인 구분 역할(CategoryCode 참조.)
}
