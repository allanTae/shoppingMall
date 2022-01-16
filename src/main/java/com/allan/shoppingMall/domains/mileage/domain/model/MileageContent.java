package com.allan.shoppingMall.domains.mileage.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 마일리지 세부 내역을 저장하기 위한 정보.
 */
@AllArgsConstructor
@Getter
public enum MileageContent {
    JOIN_MILEAGE_ACCUMULATE("회원 가입 후 마일리지 적립"),
    PAYMENT_MILEAGE_ACCUMULATE("결제 후 마일리지 적립"),
    REVIEW_MILEAGE_ACCUMULATE("리뷰 후 마일리지 적립"),
    REFUND_MILEAGE_DEDUCTION("환불 후 마일리지 차감"),
    USED_MILEAGE_DEDUCTION("사용 후 마일리지 차감");

    private String desc;
}