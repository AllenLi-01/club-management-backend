package com.example.club_management.utils;

/**
 * RESTful API 返回码
 *
 */
public interface ResponseCode {
    /**
     * 自定义业务码
     * */
    int LOGIN_SUCCESS = 20000;//登录成功
    int LOGIN_ERROR = 20004;//登录失败
    int ILLEGAL_TOKEN = 50008;//token非法
    int OTHER_LOGGED = 50012;//异地登录
    int TOKEN_EXPIRED = 50014;//token失效


    // 1xx (Informational)
    int CONTINUE = 100;
    int SWITCHING_PROTOCOLS = 101;

    // 2xx (Successful)
    int OK = 200;
    int CREATED = 201;
    int NO_CONTENT = 204;
    int PARTIAL_CONTENT = 206;

    // 3xx (Redirection)
    int MOVED_PERMANENTLY = 301;
    int FOUND = 302;
    int SEE_OTHER = 303;
    int NOT_MODIFIED = 304;
    int TEMPORARY_REDIRECT = 307;

    // 4xx (Client Error)
    int BAD_REQUEST = 400;
    int UNAUTHORIZED = 401;
    int FORBIDDEN = 403;
    int NOT_FOUND = 404;
    int UNPROCESSABLE_ENTITY = 422;

    // 5xx (Server Error)
    int INTERNAL_SERVER_ERROR = 500;
    int NOT_IMPLEMENTED = 501;
    int BAD_GATEWAY = 502;
    int SERVICE_UNAVAILABLE = 503;
    int GATEWAY_TIMEOUT = 504;
}
