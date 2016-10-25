package com.example.yuanmengzeng.hexagonblock;

import java.io.Serializable;

/**
 * 接口返回数据的模板 Created by yuanmengzeng on 2016/8/15.
 */
public class RespModel<T> implements Serializable
{
    private static final long serialVersionUID = 201608151139L;

    public int errorCode;

    public String message;

    public T result;
}
