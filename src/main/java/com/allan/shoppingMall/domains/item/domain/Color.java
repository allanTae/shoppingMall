package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum Color {

    RED(1,"빨강"), BLUE(2, "파랑"), YELLOW(3, "노랑");

    private int id;
    private String desc;

    public static Color valueOf(int id){
        switch(id){
            case 1:
                return RED;
            case 2:
                return BLUE;
            case 3:
                return YELLOW;
            default:
                throw new AssertionError("존재하지 않는 색상 정보입니다: " + id);
        }
    }
}
