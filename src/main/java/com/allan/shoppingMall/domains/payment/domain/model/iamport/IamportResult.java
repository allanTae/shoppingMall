package com.allan.shoppingMall.domains.payment.domain.model.iamport;

import lombok.Getter;
import lombok.Setter;

/**
 * 모바일 환경에서 결제 실패시, iamport 결제 api 가 전달하는 결제정보.
 */
@Getter
@Setter
public class IamportResult {

    private String imp_uid;

    private String merchant_uid;

    private boolean imp_success;

    private String error_msg;
}
