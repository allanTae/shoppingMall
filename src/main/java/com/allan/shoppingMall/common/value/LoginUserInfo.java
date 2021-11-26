package com.allan.shoppingMall.common.value;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserInfo {
    String userName;
    String userRole;
    String nickName;
    String authId;
    Long userId; // memberEntity 식별자.

    @Builder
    public LoginUserInfo(String userName, String userRole, String nickName, String authId, Long userId) {
        this.userName = userName;
        this.userRole = userRole;
        this.nickName = nickName;
        this.authId = authId;
        this.userId = userId;
    }
}
