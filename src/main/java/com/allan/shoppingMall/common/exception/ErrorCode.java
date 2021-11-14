package com.allan.shoppingMall.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Method Not allowed"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),


    // Member
    MEMBER_AUTH_ID_DUPLICATION(400, "M001", "회원 아이디가 중복되었습니다."),
    MEMBER_ID_NOT_FOUND(500, "SC001", "입력하신 아이디가 존재하지 않습니다."),
    MEMBER_INPUT_NULL_VALUE(400, "M003", "회원 정보로 null 값은 입력 하실 수 없습니다."),


    // Database
    INTEGRITY_VIOLATION(400, "D001", "Data's Itegrity is violation"),

    // Server
    REQUEST_METHOD_NOT_SUPPORTED(500, "SC001", "Request method not supoorted"),

    // Spring-Security
    LOGIN_INPUT_INVALID(400, "M002", "입력하신 아이디가 유효하지 않습니다."),
    INPUT_ID_NOT_MATCH(500, "SC002", "입력하신 아이디와 비밀번호가 일치하지 않습니다.");


    private final int status; // server status code;
    private final String code; // error 종류를 나타내는 코드.
    private final String message; // error message.
}
