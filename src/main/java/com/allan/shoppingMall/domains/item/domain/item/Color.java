package com.allan.shoppingMall.domains.item.domain.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Color {

    RED(1,"빨강"), BLUE(2, "파랑"), YELLOW(3, "노랑"),
    BLACK(4, "검정"), WHITE(5, "하양"), GRAY(6, "회색");

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
            case 4:
                return BLACK;
            case 5:
                return WHITE;
            case 6:
                return GRAY;
            default:
                throw new AssertionError("존재하지 않는 색상 정보입니다: " + id);
        }
    }
}
