package com.allan.shoppingMall.domains.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    MAN(1,"남자"),
    WOMAN(2,"여자");

    private int id;
    private String desc;

    public static Gender valueOf(int id){
        switch (id){
            case 1: return MAN;
            case 2: return WOMAN;
            default: throw new AssertionError("존재하지 않는 성별 정보 입니다. id: " + id );
        }
    }
}
