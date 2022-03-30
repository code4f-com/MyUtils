/*
 * Copyright 2022 by Tuanpla
 * https://tuanpla.com
 */
package com.tuanpla.utils.exception;

/**
 *
 * @author tuanp
 */
public class CustomerException extends Exception {

    private int code;
    private String message;

    public CustomerException(String msg) {
        this.message = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
