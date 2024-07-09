/**
 * @Descripttion: 通用Ajax请求结果对象
 * @version:
 * @Author: HuaSheng
 * @Date: 2021-03-01 12:53:00
 * @LastEditors: HuaSheng
 * @LastEditTime: 2021-03-23 12:36:56
 */
package com.czy.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

@JsonInclude(Include.NON_NULL)
public class AjaxResultVo<T> implements Serializable {

    private static final long serialVersionUID = -836653662871514751L;

    public static final int SUCCESS = 200; // 成功

    public static final int CHECK_FAIL = 400; // 参数错误

    public static final int NO_LOGIN = 401; // 未登录

    public static final int NO_PERMISSION = 403; // 禁止

    public static final int UNKNOWN_EXCEPTION = 500;

    /**
     * 是否成功
     */
    private Boolean success = false;

    /**
     * 状态码
     */
    private Integer statusCode = SUCCESS;

    /**
     * 信息
     */
    private String msg = "success";

    /**
     * 数据
     */
    private T data;

    public AjaxResultVo() {

    }

    public AjaxResultVo(Integer statusCode, String message) {
        super();
        this.statusCode = statusCode;
        this.msg = message;
    }

    public AjaxResultVo(Boolean success, Integer statusCode, String message) {
        super();
        this.success = success;
        this.statusCode = statusCode;
        this.msg = message;
    }

    public AjaxResultVo(Object data) {
        super();
        this.data = (T) data;
        this.success = Boolean.TRUE;
    }

    public static AjaxResultVo<Object> ok(Object data) {
        AjaxResultVo<Object> vo = new AjaxResultVo<>();
        vo.setSuccess(true);
        vo.setStatusCode(SUCCESS);
        vo.setMsg("success");
        vo.setData(data);
        return vo;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
