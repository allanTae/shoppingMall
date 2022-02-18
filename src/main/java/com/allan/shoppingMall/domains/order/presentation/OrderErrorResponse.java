package com.allan.shoppingMall.domains.order.presentation;

import com.allan.shoppingMall.common.exception.ErrorCode;
import lombok.*;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * server order api response,
 * response 중 에러 내용을 담고 있는 클래스 입니다.
 */
@Getter
@Setter
public class OrderErrorResponse {
    private String errorCode; // server error code(ErrorCode class 참조).
    private int status; // server response status(ErrorCode class 참조).
    private String errMsg; // 에러 메시지.
    private List<FieldError> fieldErrors; // 필드 에러 정보.


    private OrderErrorResponse(String errorCode, int status, String errMsg) {
        this.errorCode = errorCode;
        this.status = status;
        this.errMsg = errMsg;
        this.fieldErrors = new ArrayList<>();
    }

    private OrderErrorResponse(ErrorCode errorCode){
        this.errorCode = errorCode.getCode();
        this.status = errorCode.getStatus();
        this.errMsg = errorCode.getMessage();
        this.fieldErrors = new ArrayList<>();
    }

    private OrderErrorResponse(ErrorCode errorCode, List<FieldError> fieldErrors){
        this.errorCode = errorCode.getCode();
        this.status = errorCode.getStatus();
        this.errMsg = errorCode.getMessage();
        this.fieldErrors = fieldErrors;
    }


    /**
     * 외부 api 사용으로 예외 내용을 application 에서 사용하는 예외로 전달하기 위한 메소드(추후 정리 필요.)
     * @param errMsg Exception.getMessage()
     * @param errorCode ErrorCode
     * @return OrderErrorResponse
     */
    public static OrderErrorResponse of(final String errMsg, final ErrorCode errorCode){
        return new OrderErrorResponse(errorCode.getCode(), errorCode.getStatus(), errMsg);
    }


    /**
     * application 에서 정의 한 ErrorCode 만 사용하여 에러 응답 정보를 생성하는 메소드.
     * @param errorCode
     * @return
     */
    public static OrderErrorResponse of(final ErrorCode errorCode){
        return new OrderErrorResponse(errorCode);
    }

    /**
     * bidingResult 를 전달 받아 프론트단에서 전달받은 필드에 대한 에러정보를 포함하여 에러 응답 정보를 생성하는 메소드.
     * @param errorCode
     * @param bindingResult
     * @return
     */
    public static OrderErrorResponse of(final ErrorCode errorCode, final BindingResult bindingResult) {
        return new OrderErrorResponse(errorCode, FieldError.of(bindingResult));
    }

    /**
     * FieldError 정보를 담고 있는 Inner 클래스.
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        /**
         * 프론트단으로부터 전달받은 DTO 클래스의 필드 하나에 대한 Error 정보를 설정한 FieldError 리스트를 반환하는 메소드.
         * @param field
         * @param value
         * @param reason
         * @return
         */
        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        /**
         * 스프링 bingingResult 로 FieldError 리스트를 반환하는 메소드.
         * @param bindingResult
         * @return List<FiledError> filedErrorList
         */
        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
