package com.yzit.framework.web.ui;

import java.io.Serializable;

public class AjaxResult implements Serializable {
    // code--> 200 成功；  500 错误； 301 警告
    private int code  = 0;//状态码
    private boolean success = true;// 是否成功
    private String message = "操作成功";// 提示信息
    private String token = "";//token
    private Object obj = null;// 其他信息

    /**
     * 返回正确结果
     * @return
     */
    public  static  AjaxResult OK(){
        AjaxResult  ajax = new AjaxResult();
        ajax.setSuccess(true);
        ajax.setCode(200);
        ajax.setMessage("操作成功");
        return  ajax;
    }
    public  static  AjaxResult OK(String token){
        AjaxResult  ajax = new AjaxResult();
        ajax.setSuccess(true);
        ajax.setCode(200);
        ajax.setToken(token);
        ajax.setMessage("操作成功");
        return  ajax;
    }
    public  static  AjaxResult OK(Object obj){
        AjaxResult  ajax = new AjaxResult();
        ajax.setSuccess(true);
        ajax.setCode(200);
        ajax.setObj(obj);
        ajax.setMessage("操作成功");
        return  ajax;
    }
    /**
     * 操作失败，返回错误信息
     * @param message
     * @return
     */
    public static  AjaxResult  ERROR(String  message){
        AjaxResult  ajax = new AjaxResult();
        ajax.setSuccess(false);
        ajax.setCode(500);
        ajax.setMessage( message);
        return ajax;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

