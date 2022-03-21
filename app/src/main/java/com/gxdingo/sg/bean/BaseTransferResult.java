package com.gxdingo.sg.bean;

import com.zhouyou.http.model.ApiResult;

public class BaseTransferResult<T> extends ApiResult<T> {

    T info;
    @Override
    public T getData() {
        return info;
    }
    @Override
    public void setData(T data) {
        info = data;
    }

}
