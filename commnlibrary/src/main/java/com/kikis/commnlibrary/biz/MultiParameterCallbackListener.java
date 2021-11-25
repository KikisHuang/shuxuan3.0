package com.kikis.commnlibrary.biz;

/**
 * 多参数回调监听接口
 *
 * @author JM
 */
public interface MultiParameterCallbackListener {
    /**
     * 多数据回调
     * @param objects 数组
     */
    void multipleDataResult(Object... objects);
}
