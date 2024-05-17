package com.gordon.vc.restful.util;

public class BizException extends RuntimeException {
    private final String msg;

    public BizException(String msg) {
        this.msg = msg;
    }

    public BizException(String message, String msg) {
        super(message);
        this.msg = msg;
    }

    public BizException(String message, Throwable cause, String msg) {
        super(message, cause);
        this.msg = msg;
    }

    public BizException(Throwable cause, String msg) {
        super(cause);
        this.msg = msg;
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String msg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BizException{" +
                "msg='" + msg + '\'' +
                "} " + super.toString();
    }

    public String getMsg() {
        return msg;
    }
}
