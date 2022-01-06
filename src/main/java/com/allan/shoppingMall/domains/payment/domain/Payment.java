package com.allan.shoppingMall.domains.payment.domain;

import com.allan.shoppingMall.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * iamport 기반.
 * 결제 정보를 저장 할 도메인.
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payments")
@Slf4j
public class Payment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    // 결제번호.
    // 아임포트 결제 고유번호(iamport api 에서 제공하는 imp_uid).
    @Column(name = "payment_num", unique = true, nullable = false)
    private String paymentNum;

    // 주문번호.
    // order 를 조회 할 때 사용 할 주문번호(iamport api 에서 제공하는 merchant_uid).
    // 환불시, 사용할 주문번호.
    @Column(name = "order_num", unique = true, nullable = false)
    private String orderNum;

    // 결제 수단.
    @Column(name = "pay_method", nullable = false)
    private String payMethod;

    // 결제 금액.
    @Column(name = "pay_amount", nullable = false)
    private Long payAmount;

    // 결제 상태.
    @Column(name = "pay_status", nullable = false)
    private String payStatus;

    // 주문명.
    @Column(name = "order_name", nullable = false)
    private String orderName;

    // 환불 된 금액.
    @Column(name = "cancel_amount", nullable = false)
    private Long cancelAmount;

    @PrePersist
    public void setUp(){
        this.cancelAmount = 0l;
    }

    @Builder
    public Payment(String paymentNum, String orderNum, String payMethod, Long payAmount, String payStatus, String orderName) {
        this.paymentNum = paymentNum;
        this.orderNum = orderNum;
        this.payMethod = payMethod;
        this.payAmount = payAmount;
        this.payStatus = payStatus;
        this.orderName = orderName;
    }

    /**
     * 환불금액을 수정하는 메소드입니다.
     * @param cancelAmount 전달 된 환불금액.
     */
    public void changeCancelAmount(Long cancelAmount){
        this.cancelAmount = cancelAmount;
    }
}
