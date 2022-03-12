package com.allan.shoppingMall.domains.order.domain.model;

import com.allan.shoppingMall.domains.order.presentation.OrderErrorResponse;
import com.allan.shoppingMall.domains.payment.domain.model.iamport.IamportResult;
import lombok.Getter;
import lombok.Setter;

/**
 * orderForm.jsp 에서 서버측으로 주문 결과 정보를 전달하기 위해 사용되는 오브젝트.
 */
@Getter
@Setter
public class OrderResultRequest {

    // 주문결과
    private String orderResult;

    // 주문번호.
    private String orderNum;

    // 주문에러.
    private OrderErrorResponse errorResponse;

    // iamport result
    IamportResult result;

    @Override
    public String toString() {
        return "OrderResultRequest{" +
                "orderResult='" + orderResult + '\'' +
                ", orderNum='" + orderNum + '\'' +
                '}';
    }
}
