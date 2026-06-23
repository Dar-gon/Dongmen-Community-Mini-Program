package com.community.exception;

/**
 * 资源不存在异常
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super("资源不存在");
    }
}
