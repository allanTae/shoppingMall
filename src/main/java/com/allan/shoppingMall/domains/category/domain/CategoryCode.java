package com.allan.shoppingMall.domains.category.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Item 도메인별 구분 조회를 위한 카테고리 구분을 위한 코드 enum.
 */
@Getter
@AllArgsConstructor
public enum CategoryCode {
    ROOT(1, "root"),
    CLOTHES(2, "clothes"),
    ACCESSORY(3, "accessory");

    private int code;
    private String desc;

    public static CategoryCode valueOf(int code){
        switch (code){
            case 1:
                return ROOT;
            case 2:
                return CLOTHES;
            case 3:
                return ACCESSORY;
            default:
                throw new AssertionError("존재하지 않는 카테고리 정보입니다: " + code);
        }
    }
}
