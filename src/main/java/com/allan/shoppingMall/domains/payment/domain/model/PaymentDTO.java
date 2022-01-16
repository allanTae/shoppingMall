package com.allan.shoppingMall.domains.payment.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 결제 요약정보를 전달하기 위한 Object
 */
@Getter
@Setter
public class PaymentDTO {
    // 결제 방식.
    String payMethod;

    // 카드 이름.
    String cardName;

    // 카드 번호.
    String cardNum;

    // 은행사 이름.
    String bankName;

    // 총 결제 금액.
    Long totalAmount;

    // 상품 금액.
    Long itemAmount;

    // 배송비.
    Long deliveryAmount;

    // 할인 금액.
    Long discountAmount;

    // 사용한 마일리지.
    Long mileagePoint;

    @Builder
    public PaymentDTO(String payMethod, Long totalAmount, Long itemAmount, Long deliveryAmount) {
        setPayMethod(payMethod);
        this.totalAmount = totalAmount;
        this.itemAmount = itemAmount;
        this.deliveryAmount = deliveryAmount;
    }

    private void setPayMethod(String payMethod){
        if(payMethod.equals("card"))
            this.payMethod = "카드결제";
    }

    public void setCardInfo(String cardNum, String cardName){
        this.cardNum = cardNum;
        this.cardName = cardName;
    }

    public void setBankInfo(String bankName){
        this.bankName = bankName;
    }
}
