package com.allan.shoppingMall.domains.order.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 주문시, 로그인한 회원 정보가 아닌 사용자 입력 받은 주문자 정보를 활용하기 위한 object.
 */

@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrdererInfo {

    @Column(name = "orderer_name", nullable = false)
    private String ordererName;

    @Column(name = "orderer_phone", nullable = false)
    private String ordererPhone;

    @Column(name = "orderer_email", nullable = false)
    private String ordererEmail;

    @Builder
    public OrdererInfo(String ordererName, String ordererPhone, String ordererEmail) {
        this.ordererName = ordererName;
        this.ordererPhone = ordererPhone;
        this.ordererEmail = ordererEmail;
    }
}
