package com.allan.shoppingMall.domains.mileage.domain;

import com.allan.shoppingMall.common.domain.BaseEntity;
import com.allan.shoppingMall.domains.mileage.domain.model.MileageContent;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 마일리지 도메인입니다.
 * 마일리지는 회원 도메인과, 주문 도메인과 연관 관계가 있지만 참조 무결성에 의존하지 않도록 하기 위해서
 * 회원 도메인과 주문 도메인 연관관계를 맺지 않고, 구분자(사용자 아이디, 주문번호)만 값을 연관되도록 처리.
 * 1) 사용자아이디는 어떠한 회원과 관련된 마일리지인지 구분하기 위한 요소.
 * 2) 주문번호는 어떠한 주문과 관련된 마일리지인지 구분하기 위한 요소.
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mileages")
public class Mileage extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mileageId;

    // 적립 되는 회원 아이디.
    @Column(name ="auth_id", nullable = false)
    private String authId;

    // 적립 되는 주문에 대한 아이디.
    @Column(name = "order_num", nullable = false)
    private String orderNum;

    // 마일리지 적립내용.
    @Column(name = "mileage_content", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MileageContent mileageContent;

    // 마일리지 포인트.
    @Column(nullable = false)
    private Long point;

    /**
     * 회원가입시, 적립 되는 마일리지는 주문번호가 존재하지 않습니다.
     * 회원가입시, 적립 마일리지 주문번호는 'joinMember' 값으로 저장합니다.
     */
    @PrePersist
    public void setUp(){
        if(this.mileageContent == MileageContent.JOIN_MILEAGE_ACCUMULATE){
            if(this.getOrderNum() == null || this.getOrderNum().equals(""))
                this.orderNum = "joinMember";
        }
    }

    @Builder
    public Mileage(String authId, String orderNum, MileageContent mileageContent, Long point) {
        this.authId = authId;
        this.orderNum = orderNum;
        this.mileageContent = mileageContent;
        this.point = point;
    }


}
