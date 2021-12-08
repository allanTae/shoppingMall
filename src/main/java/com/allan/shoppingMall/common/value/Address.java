package com.allan.shoppingMall.common.value;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(name = "jibun_address")
    private String jibunAddress;

    @Column(name = "road_address", nullable = false)
    private String roadAddress;

    @Column(name = "post_code", nullable = false)
    private String postCode;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    @Column(name = "extra_address")
    private String extraAddress;

    @Builder
    public Address(String jibunAddress, String roadAddress, String postCode, String detailAddress, String extraAddress){
        this.jibunAddress = jibunAddress;
        this.roadAddress = roadAddress;
        this.postCode = postCode;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
    }
}
