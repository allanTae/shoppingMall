package com.allan.shoppingMall.domains.payment.domain;

import com.allan.shoppingMall.common.domain.BaseEntity;

import javax.persistence.*;

@Entity
public class Refund extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refundId;

    @Column(name = "refund_amount")
    private Long refundAmount;
}
