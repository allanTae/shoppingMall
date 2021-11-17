package com.allan.shoppingMall.common.api.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberResponse {

    IN_NOT_USE("in_not_use", "사용 가능한 아이디 입니다."),
    IN_USE("in_use", "현재 사용중인 아이디 입니다.");

    public String status;
    public String message;
}
