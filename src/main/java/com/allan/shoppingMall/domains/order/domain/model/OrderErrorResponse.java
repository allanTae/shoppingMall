package com.allan.shoppingMall.domains.order.domain.model;

import com.allan.shoppingMall.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * server api response, 에러 내용을 담고 있는 클래스 입니다.
 */
@Getter
@Setter
public class OrderErrorResponse {
    private String errorCode; // server error code(ErrorCode class 참조).
    private int status; // server response status(ErrorCode class 참조).
    private String errMsg; // 에러 메시지.

    public OrderErrorResponse(String errorCode, int status, String errMsg) {
        this.errorCode = errorCode;
        this.status = status;
        this.errMsg = errMsg;
    }

    /**
     * @param errMsg Exception.getMessage()
     * @param errorCode ErrorCode
     * @return OrderErrorResponse
     */
    public static OrderErrorResponse of(final String errMsg, final ErrorCode errorCode){
        return new OrderErrorResponse(errorCode.getCode(), errorCode.getStatus(), errMsg);
    }


}
