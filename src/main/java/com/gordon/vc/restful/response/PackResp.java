package com.gordon.vc.restful.response;

public class PackResp<T> {
    private boolean success = true;
    private int code;
    private String msg = "success";
    private T respData;

    public static <T> PackResp<T> fail(String msg) {
        PackResp<T> resp = new PackResp<>();
        resp.setCode(500);
        resp.setMsg(msg);
        resp.setSuccess(false);
        return resp;
    }

    public static <T> PackResp<T> fail() {
        PackResp<T> resp = new PackResp<>();
        resp.setCode(500);
        resp.setMsg("fail");
        resp.setSuccess(false);
        return resp;
    }

    public static <T> PackResp<T> success(T respData) {
        PackResp<T> resp = new PackResp<>();
        resp.setCode(200);
        resp.setRespData(respData);
        return resp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getRespData() {
        return respData;
    }

    public void setRespData(T respData) {
        this.respData = respData;
    }

    @Override
    public String toString() {
        return "PackResp{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", respData=" + respData +
                '}';
    }
}
