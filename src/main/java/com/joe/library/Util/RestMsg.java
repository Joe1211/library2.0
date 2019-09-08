package com.joe.library.Util;

/**
 * @authr 乔
 * @Date 2019/9/8
 */
public class RestMsg<T> {

    public static final int RECODE_OK = 200;
    public static final  int RECODE_FAIL = 400;


    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    /**
     * 成功、失败、错误等状态码
     */
    private int code;

    /**
     * 失败、错误时的额外信息
     */
    private String msg;

    /**
     * 输出
     */
    private T result;

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

    public RestMsg<T> successMsg(){
        this.code = RECODE_OK;
        this.msg = SUCCESS;
        return this;
    }

    public RestMsg<T> successMsg(String successMsg){
        this.code = RECODE_OK;
        this.msg = successMsg;
        return this;
    }

    public RestMsg<T> errorMsg() {
        this.code = RECODE_FAIL;
        this.msg = ERROR;
        return this;
    }

    public RestMsg<T> errorMsg(String errorMsg) {
        this.code = RECODE_FAIL;
        this.msg = errorMsg;
        return this;
    }

    @Override
    public String toString() {
        return "RestMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }
}
