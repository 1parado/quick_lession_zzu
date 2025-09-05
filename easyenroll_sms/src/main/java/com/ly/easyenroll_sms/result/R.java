package com.ly.easyenroll_sms.result;

//一用Lomback就出错

public class R<T> {
    private int code;
    private String message;
    private T data;

    public static <T> R<T> success(T data) {
        R<T> result = new R<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> R<T> success(T data, String message) {
        R<T> result = new R<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static R<?> error(int code, String message) {
        R<?> result = new R<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static R<?> error(String message) {
        R<?> result = new R<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public R() {
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}