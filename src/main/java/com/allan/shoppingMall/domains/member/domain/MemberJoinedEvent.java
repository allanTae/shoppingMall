package com.allan.shoppingMall.domains.member.domain;

import lombok.*;

/**
 * 회원 가입을 할 때, 가입 이벤트 클래스입니다.
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
public class MemberJoinedEvent {
    private Long memberId;
}
