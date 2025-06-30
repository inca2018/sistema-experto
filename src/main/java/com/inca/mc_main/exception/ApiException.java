package com.inca.mc_main.exception;

public class ApiException extends RuntimeException {

    private final boolean status;
    private final int code;
    private final Object data;

    public ApiException(boolean status, int code, String message, Object data) {
        super(message);
        this.status = status;
        this.code = code;
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
