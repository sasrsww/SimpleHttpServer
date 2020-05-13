package com.sww.bean;

/**
 * Result
 *
 * @author shenwenwen
 * @date 2020/4/30 18:30
 */
public class Result<T> {

    private static final boolean DEFAULT_SUCCESS_VALUE = false;
    private static final String EMPTY = "";

    private boolean success = DEFAULT_SUCCESS_VALUE;
    private String errorMsg = "";
    private String errorCode = "";
    private T model;

    public Result(boolean success, String errorMsg, String errorCode, T model) {
        this.success = success;
        if (null != errorMsg) {
            this.errorMsg = errorMsg;
        }
        if (null != errorCode) {
            this.errorCode = errorCode;
        }
        if (null != model) {
            this.model = model;
        }
    }

    public static <T> Result<T> success() {
        return new Result(Boolean.TRUE, EMPTY, EMPTY, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result(Boolean.TRUE, EMPTY, EMPTY, data);
    }

    public static <T> Result<T> failed() {
        return new Result(Boolean.FALSE, EMPTY, EMPTY, null);
    }

    public static <T> Result<T> failed(String errorMsg) {
        return new Result(Boolean.FALSE, errorMsg, "", null);
    }

    public static <T> Result<T> failed(String errorMsg, String errorCode) {
        return new Result(Boolean.FALSE, errorMsg, errorCode, null);
    }

    public static <T> Result<T> failed(String errorMsg, String errorCode, T data) {
        return new Result(Boolean.FALSE, errorMsg, errorCode, data);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

}
