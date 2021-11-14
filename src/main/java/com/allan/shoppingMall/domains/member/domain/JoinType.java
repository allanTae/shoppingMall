package com.allan.shoppingMall.domains.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JoinType {

    NORMAL("service", "system", "일반 서비스를 이용한 가입유형");

    private String type; // 가입 유형.
    private String detail; // 세부 가입 종류.
    private String desc;
}
