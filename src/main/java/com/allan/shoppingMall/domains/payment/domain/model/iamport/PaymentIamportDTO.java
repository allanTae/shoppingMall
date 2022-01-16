package com.allan.shoppingMall.domains.payment.domain.model.iamport;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * IamPort api 로 조회 한 payment를
 * 서비스단에서 사용하기 위한 데이터만 가지고 있는 DTO object
 */

@Getter
@Setter
public class PaymentIamportDTO {
    private Long paymentAmount; // 서비스단에서 유효성 검사를 할 때 사용 할 결제 총금액.
    private String impUid;
    private String merchantUid;
    private String payStatus;
    private String payMethod;
    private String name;

    @Builder
    public PaymentIamportDTO(Long paymentAmount, String impUid, String merchantUid, String payStatus, String payMethod, String name) {
        this.paymentAmount = paymentAmount;
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.payStatus = payStatus;
        this.payMethod = payMethod;
        this.name = name;
    }
}
