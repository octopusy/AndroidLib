package com.axl.android.frameworkbase.net.model;

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2017/10/25
 * Time: 10:20
 * 请求返回实体基类
 */

public class BaseResultModel<T> {
    public int code;
    public String msg;
    public T data;
}
