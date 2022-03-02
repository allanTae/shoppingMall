package com.allan.shoppingMall.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * server 단에서 비즈니스 로직을 처리할 때 발생한 에러에 대한 정보를 담고 있습니다.
 * Business Exception 을 통해서 전달 되며,
 * api response 에 포함되어 사용자에게 에러 정보를 전달 할 때 사용합니다.
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Method Not allowed"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    // JPA
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),

    // Member
    MEMBER_AUTH_ID_DUPLICATION(400, "M001", "회원 아이디가 중복되었습니다."),
    MEMBER_ID_NOT_FOUND(500, "SC001", "입력하신 아이디가 존재하지 않습니다."),
    MEMBER_INPUT_NULL_VALUE(400, "M003", "회원 정보로 null 값은 입력 하실 수 없습니다."),

    // Order
    ORDER_CANCEL_NOT_ALLOWED(400, "OR001", "주문 취소 가능한 상태가 아닙니다."),
    ORDER_REFUND_NOT_ALLOWED(400, "OR002", "환불 가능한 주문 상태가 아닙니다."),
    ORDER_NOT_FOUND(400, "OR003", "조회 된 주문이 없습니다."),
    INVALID_ORDER_REQUEST_INPUT_VALUE(400, "OR004", "요청에 알맞지 않은 정보입니다."),

    // Payment
    PAYMENT_AMOUNT_IS_NOT_EQUAL_BY_ORDER_AMOUNT(400, "PAY001", "결제금액과 주문금액이 일치하지 않습니다."),
    PAYMENT_INVALID_ORDER_STATUS(400, "PAY002", "결제가능한 주문 상태가 아닙니다."),
    PAYMENT_NOT_FOUND(400, "PAY002", "조회 된 결제가 없습니다."),

    // Mileage
    MILEAGE_POINT_NOT_ENOUGH_FOR_DEDUCT(400, "MILE001", "차감 할 마일리지 포인트가 부족합니다."),
    MILEAGE_NOT_FOUND(400, "MILE002", "죄회 된 마일리지가 없습니다."),

    // Item
    ITEM_STOCK_QUANTITY_EXCEEDED(400, "I001", "상품의 재고량이 부족합니다."),
    ITEM_CATEGORY_CODE_INVALID(400, "I002", "상품에 대한 카테고리가 유효하지 않습니다."),

    // Database
    INTEGRITY_VIOLATION(400, "D001", "Data's Itegrity is violation"),

    // Server
    REQUEST_METHOD_NOT_SUPPORTED(500, "SC001", "Request method not supoorted"),

    // Spring-Security
    LOGIN_FAIL(400, "SECURITY1", "잘못된 아이디 또는, 비밀번호입니다."),
    LOGIN_INPUT_INVALID(400, "M002", "입력하신 아이디가 유효하지 않습니다."),
    INPUT_ID_NOT_MATCH(500, "SC002", "입력하신 아이디와 비밀번호가 일치하지 않습니다."),

    // Iamport
    IAMPORT_ERROR(400, "IAMPORT001", "Iamport 관련 에러입니다."),

    // Cart
    MEMBER_CART_NOT_FOUND(400, "CART001", "조회 된 회원 장바구니가 없습니다."),
    INPUT_CART_ITEM_QUANTITY_LESS_THAN_MINIMUM_VALUE(400, "CART002", "장바구니에 담는 상품의 최소 수량은 1개입니다."),
    TEMP_CART_NOT_FOUND(400, "CART003", "조회 된 비회원 장바구니가 없습니다."),

    // Category
    CATEGORY_BRANCH_AND_NAME_IS_DUPLICATION(400, "CATE001", "이미 사용 중인 대분류 그룹과 이름입니다.");

    private final int status; // server status code;
    private final String code; // error 종류를 나타내는 코드.
    private final String message; // error message.
}
