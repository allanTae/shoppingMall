package com.allan.shoppingMall.common.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {

    private Long memberId;
    private String name;
    private String email;
    private String phone;

    @Builder
    public UserInfo(Long memberId, String name, String email, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
