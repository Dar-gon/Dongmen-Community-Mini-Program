package com.community.exception;

/**
 * 禁止访问异常
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super("权限不足");
    }
}
