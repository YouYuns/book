package com.shop.book.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "클라이언트 요청의 형식이나 내용이 잘못된 경우"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER", "요청한 파라미터의 형식이나 내용에 오류가 있는 경우"),
    INVALID_RESOURCE(HttpStatus.BAD_REQUEST, "INVALID_RESOURCE", "요청한 리소스의 내용에 오류가 있는 경우"),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "MISSING_PARAMETER", "필수 파라미터를 지정하지 않은 경우"),
    LIMIT_EXCEEDED_400(HttpStatus.BAD_REQUEST, "LIMIT_EXCEEDED", "제한 설정을 초과한 경우"),
    OUT_OF_RANGE_400(HttpStatus.BAD_REQUEST, "OUT_OF_RANGE", "요청 값이 범위를 벗어나는 경우"),

    // 401 Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "요구되는 인증 정보가 누락되었거나 잘못됨"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Access Token이 잘못된 경우"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", "Token 기간 만료"),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "NOT_VALID_TOKEN", "유효한 Token 아닐 경우"),
    REFRESH_TOKEN_INVALD(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_INVALD", "Refresh Token INVALD"),
    

    // 403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "요청은 이해했지만 권한이 없어 거부됨"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "요청한 리소스에 대한 접근이 제한됨"),
    LIMIT_EXCEEDED_403(HttpStatus.FORBIDDEN, "LIMIT_EXCEEDED", "리소스 제한을 초과한 경우"),
    OUT_OF_RANGE_403(HttpStatus.FORBIDDEN, "OUT_OF_RANGE", "요청 범위를 벗어난 경우"),
    INSUFFICIENT_SCOPE(HttpStatus.FORBIDDEN, "INSUFFICIENT_SCOPE", "Access Token에 필요한 Scope 권한이 없음"),
    

    // 404 Not Found
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "요청한 리소스를 찾을 수 없음"),
    RESOURCE_NOT_EXIST(HttpStatus.NOT_FOUND, "RESOURCE_NOT_EXIST", "특정 리소스를 찾을 수 없음"),
    NOT_MATCH_USER_ACCESSTOKEN_REFRESHTOKEN( HttpStatus.NOT_FOUND, "NOT_MATCH_USER_ACCESSTOKEN_REFRESHTOKEN", "만료된 accessToken의 주인과 입력한 refresh token의 유저가 같지않음"),
    // 409 Conflict
    CONFLICT(HttpStatus.CONFLICT, "CONFLICT", "요청한 리소스와 충돌 발생"),
    ALREADY_EXIST(HttpStatus.CONFLICT, "ALREADY_EXIST", "요청한 리소스가 이미 존재함"),
    RESOURCE_ALREADY_EXIST(HttpStatus.CONFLICT, "RESOURCE_ALREADY_EXIST", "특정 리소스가 이미 존재함"),

    // 410 Gone
    DELETED(HttpStatus.GONE, "DELETED", "요청한 리소스가 삭제되어 존재하지 않음"),

    // 500 Internal Server Error
    USER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "USER_NOT_FOUND", "유저 정보가 존재하지 않습니다."),
    PAASSWORD_NOT_MATCH(HttpStatus.INTERNAL_SERVER_ERROR, "PAASSWORD_NOT_MATCH", "유저 패스워드가 일치하지 않습니다."),
    USER_DUPLICATE(HttpStatus.INTERNAL_SERVER_ERROR, "USER_DUPLICATE" , "이미 같은 이메일로 가입되어있습니다"),
    NOT_BOOK_DETAIL_INFO(HttpStatus.INTERNAL_SERVER_ERROR, "NOT_BOOK_DETAIL_INFO","도서 정보를 가져오는 중 오류가 발생했습니다." ),

    // 503 Service Unavailable
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE", "일시적인 서버 오류");

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
