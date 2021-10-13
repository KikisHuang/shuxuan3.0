package com.gxdingo.sg.bean;

/**
 * @author: Kikis
 * @date: 2021/5/19
 * @page:
 */
public class MessageSubsBean {

    /**
     * subscribes : {"list":[{"fromAvatar":"http://oss.dgkjmm.com/upload/20200730/643fe7fed3d7438eb767c86f75471dce.jpg","fromName":"zhangsan","fromType":10,"id":8,"lastMsg":"没有了","lastMsgTime":"2020-08-26 17:32:29","order":{"id":3541,"storeAvatar":"http://xxxxxxx.com/image.jpg","storeId":35,"storeName":"店铺名称","tradeNo":210235413333},"unreadNum":1},{"fromAvatar":"http://oss.dgkjmm.com/upload/20200730/643fe7fed3d7438eb767c86f75471dce.jpg","fromName":"zhangsan","fromType":20,"id":9,"lastMsg":"没有了","lastMsgTime":"2020-08-26 17:32:29","unreadNum":1}],"total":12,"totalUnread":22,"wsServerUrl":"ws://xx.xx.xx.xx/socketio"}
     */

    private SubscribesBean subscribes;

    public SubscribesBean getSubscribes() {
        return subscribes;
    }

    public void setSubscribes(SubscribesBean subscribes) {
        this.subscribes = subscribes;
    }

}
