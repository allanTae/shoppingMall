package com.allan.shoppingMall.domains.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

    ACTIVATED_USER("ROLE_USER", "user", JoinType.NORMAL, "activated"),
    ACTIVATED_ADMIN("ROLE_ADMIN", "admin", JoinType.NORMAL, "activated"),
    REMOVED_USER("ROLE_USER", "user", JoinType.NORMAL, "removed"),
    REMOVED_ADMIN("ROLE_ADMIN", "admin", JoinType.NORMAL, "removed"),
    DORMANT_USER("ROLE_USER", "user", JoinType.NORMAL, "dormant"),
    DORMANT_ADMIN("ROLE_ADMIN", "admin", JoinType.NORMAL, "dormant");

    private String securityKey; // spring security role 구분.
    private String roleDetail; // application 에서 사용 할 세부 role 정보.
    private JoinType joinType; // 가입유형.
    private String state; // 현재 회원 상태 값.(activated: 사용가능한 상태, removed: 삭제된 상태, dormant: 휴면 상태)
}
