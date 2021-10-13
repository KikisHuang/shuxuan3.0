package com.gxdingo.sg.bean;


/**
 * @author: Kikis
 * @date: 2020/12/28
 * @page:
 */
public class NoticesBean<T> {


    /**
     * type : reply
     * msg : 请求成功
     * code : 0
     * data : {"newsList":[{"subscribeId":2,"fromId":8,"fromName":"很好","fromAvatar":"http://oss.dgkjmm.com/upload/20200730/643fe7fed3d7438eb767c86f75471dce.jpg","fromType":10,"content":"消息内容","msgType":0,"msgTime":"2020-08-26 17:32:29"}]}
     */

    private String type;
    private String msg;
    private int code;
    private T data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
