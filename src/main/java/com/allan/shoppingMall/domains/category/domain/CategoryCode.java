package com.allan.shoppingMall.domains.category.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Item 도메인별 구분 조회(카테고리 구분을 위한 코드) enum.
 * Item 도메인은 Clothes, Accessory 등의 형태로 상속받아 각자의 타입이 다르기에 상품 상세 조회시,
 * 각 상품 도메인을 구분하기 위한 Enum class 입니다.
 */
@Getter
@AllArgsConstructor
public enum CategoryCode {
    ROOT(1, "root"),
    CLOTHES(2, "clothes"),
    ACCESSORY(3, "accessory"),
    SHOP(4, "shop");

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
            case 4:
                return SHOP;
            default:
                throw new AssertionError("존재하지 않는 카테고리 정보입니다: " + code);
        }
    }
}
