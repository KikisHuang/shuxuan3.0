package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Kikis
 * @date: 2021/5/20
 * @page:
 */
public class MessageDetails {


    /**
     * messages : {"list":[{"content":"订单支付超时，已为您自动取消","fromId":30202,"msgTime":"2021-06-07 09:20:02","order":{"id":146,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041511926921},"type":21},{"content":"订单支付超时，已为您自动取消","fromId":30202,"msgTime":"2021-06-07 09:20:02","order":{"id":149,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041535745651},"type":21},{"content":"订单支付超时，已为您自动取消","fromId":30202,"msgTime":"2021-06-07 09:20:02","order":{"id":150,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041539425130},"type":21},{"content":"订单支付超时，已为您自动取消","fromId":30202,"msgTime":"2021-06-07 09:20:02","order":{"id":151,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041543887645},"type":21},{"content":"订单支付超时，已为您自动取消","fromId":30202,"msgTime":"2021-06-07 09:20:02","order":{"id":152,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041555882452},"type":21},{"content":"订单支付超时，已为您自动取消","fromId":30202,"msgTime":"2021-06-07 09:20:02","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"商家提醒您，有订单未付款。系统将在2021-06-04 17:18:38关闭订单","fromId":30202,"msgTime":"2021-06-04 17:38:48","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"商家提醒您，有订单未付款。系统将在2021-06-04 17:18:38关闭订单","fromId":30202,"msgTime":"2021-06-04 17:38:43","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 17:08:38","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 16:22:22","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 16:22:12","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"您有订单被商家拒绝，拒绝原因：\u201c不可抗力因素原因\u201d，是否选择随 机发送商家？","fromId":30202,"msgTime":"2021-06-04 16:21:36","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 16:21:24","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"订单已送达，赶快来评价！","fromId":30202,"msgTime":"2021-06-04 16:03:30","order":{"id":153,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041556976315},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 16:02:26","order":{"id":152,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041555882452},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 15:56:48","order":{"id":153,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041556976315},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:56:33","order":{"id":153,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041556976315},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:56:21","order":{"id":153,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041556976315},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:55:21","order":{"id":152,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041555882452},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:55:01","order":{"id":152,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041555882452},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 15:44:54","order":{"id":151,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041543887645},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:44:19","order":{"id":151,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041543887645},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:43:22","order":{"id":151,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041543887645},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 15:41:50","order":{"id":150,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041539425130},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:40:23","order":{"id":150,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041539425130},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:39:53","order":{"id":150,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041539425130},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 15:38:30","order":{"id":149,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041535745651},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:38:04","order":{"id":148,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041524175688},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:37:56","order":{"id":149,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041535745651},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:35:21","order":{"id":149,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041535745651},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:24:38","order":{"id":148,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041524175688},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 15:13:42","order":{"id":146,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041511926921},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:11:59","order":{"id":146,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041511926921},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:11:33","order":{"id":146,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041511926921},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:07:56","order":{"id":128,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041001359431},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:03:59","order":{"id":144,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041451741013},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 14:51:51","order":{"id":144,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041451741013},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 10:01:12","order":{"id":128,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041001359431},"type":21},{"content":"您有订单商家暂时无法配送，是否选择随 机发送商家？","fromId":30202,"msgTime":"2021-06-03 18:05:18","order":{"id":110,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106031802538685},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-03 18:02:14","order":{"id":110,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106031802538685},"type":21},{"content":"订单已送达，赶快来评价！","fromId":30202,"msgTime":"2021-06-03 17:47:13","order":{"id":108,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106031742405712},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-03 17:42:48","order":{"id":108,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106031742405712},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-03 17:42:36","order":{"id":108,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106031742405712},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-03 17:42:05","order":{"id":108,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106031742405712},"type":21},{"content":"订单已送达，赶快来评价！","fromId":30202,"msgTime":"2021-06-02 17:38:49","order":{"id":90,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106021011188923},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-02 10:15:52","order":{"id":90,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106021011188923},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-02 10:15:42","order":{"id":90,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106021011188923},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-01 17:32:47","order":{"id":89,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106011732259042},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-01 17:32:39","order":{"id":89,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106011732259042},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-01 17:30:27","order":{"id":72,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2105311615807838},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-01 16:18:50","order":{"id":79,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106011441438869},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-01 16:18:43","order":{"id":79,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106011441438869},"type":21},{"content":"您有订单被商家拒绝，拒绝原因：\u201c不可抗力因素原因\u201d，是否选择随 机发送商家？","fromId":30202,"msgTime":"2021-06-01 15:58:48","order":{"id":79,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106011441438869},"type":21},{"content":"您有订单被商家拒绝，拒绝原因：\u201c距离过远无法配送\u201d，是否选择随 机发送商家？","fromId":30202,"msgTime":"2021-06-01 15:38:26","order":{"id":80,"storeAvatar":"","storeId":"","storeName":"","tradeNo":2106011447443792},"type":21}],"total":0}
     * subscribe : {"fromAvatar":"https://www.gxdingo.com/static/images/app/icon_delivery1088.png","fromId":30202,"fromName":"交易物流","fromType":30,"id":112,"toAvatar":"http://oss.dgkjmm.com/upload/2021-05-12 16:20:08/b5a233e8fa414d8d9d2184edd2bdabc4.jpeg","toId":10708,"toName":"巴扎黑"}
     */

    private MessagesBean messages;
    private SubscribeBean subscribe;

    public MessagesBean getMessages() {
        return messages;
    }

    public void setMessages(MessagesBean messages) {
        this.messages = messages;
    }

    public SubscribeBean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(SubscribeBean subscribe) {
        this.subscribe = subscribe;
    }

    public static class MessagesBean {
        /**
         * list : [{"content":"订单支付超时，已为您自动取消","fromId":30202,"msgTime":"2021-06-07 09:20:02","order":{"id":146,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041511926921},"type":21},{"content":"订单支付超时，已为您自动取消","fromId":30202,"msgTime":"2021-06-07 09:20:02","order":{"id":149,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041535745651},"type":21},{"content":"订单支付超时，已为您自动取消","fromId":30202,"msgTime":"2021-06-07 09:20:02","order":{"id":150,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041539425130},"type":21},{"content":"订单支付超时，已为您自动取消","fromId":30202,"msgTime":"2021-06-07 09:20:02","order":{"id":151,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041543887645},"type":21},{"content":"订单支付超时，已为您自动取消","fromId":30202,"msgTime":"2021-06-07 09:20:02","order":{"id":152,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041555882452},"type":21},{"content":"订单支付超时，已为您自动取消","fromId":30202,"msgTime":"2021-06-07 09:20:02","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"商家提醒您，有订单未付款。系统将在2021-06-04 17:18:38关闭订单","fromId":30202,"msgTime":"2021-06-04 17:38:48","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"商家提醒您，有订单未付款。系统将在2021-06-04 17:18:38关闭订单","fromId":30202,"msgTime":"2021-06-04 17:38:43","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 17:08:38","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 16:22:22","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 16:22:12","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"您有订单被商家拒绝，拒绝原因：\u201c不可抗力因素原因\u201d，是否选择随 机发送商家？","fromId":30202,"msgTime":"2021-06-04 16:21:36","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 16:21:24","order":{"id":160,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041621709709},"type":21},{"content":"订单已送达，赶快来评价！","fromId":30202,"msgTime":"2021-06-04 16:03:30","order":{"id":153,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041556976315},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 16:02:26","order":{"id":152,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041555882452},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 15:56:48","order":{"id":153,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041556976315},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:56:33","order":{"id":153,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041556976315},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:56:21","order":{"id":153,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041556976315},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:55:21","order":{"id":152,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041555882452},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:55:01","order":{"id":152,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041555882452},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 15:44:54","order":{"id":151,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041543887645},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:44:19","order":{"id":151,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041543887645},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:43:22","order":{"id":151,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041543887645},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 15:41:50","order":{"id":150,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041539425130},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:40:23","order":{"id":150,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041539425130},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:39:53","order":{"id":150,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041539425130},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 15:38:30","order":{"id":149,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041535745651},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:38:04","order":{"id":148,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041524175688},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:37:56","order":{"id":149,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041535745651},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:35:21","order":{"id":149,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041535745651},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:24:38","order":{"id":148,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041524175688},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-04 15:13:42","order":{"id":146,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041511926921},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:11:59","order":{"id":146,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041511926921},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 15:11:33","order":{"id":146,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041511926921},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:07:56","order":{"id":128,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041001359431},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-04 15:03:59","order":{"id":144,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041451741013},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 14:51:51","order":{"id":144,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041451741013},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-04 10:01:12","order":{"id":128,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041001359431},"type":21},{"content":"您有订单商家暂时无法配送，是否选择随 机发送商家？","fromId":30202,"msgTime":"2021-06-03 18:05:18","order":{"id":110,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106031802538685},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-03 18:02:14","order":{"id":110,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106031802538685},"type":21},{"content":"订单已送达，赶快来评价！","fromId":30202,"msgTime":"2021-06-03 17:47:13","order":{"id":108,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106031742405712},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-03 17:42:48","order":{"id":108,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106031742405712},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-03 17:42:36","order":{"id":108,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106031742405712},"type":21},{"content":"下单成功，等待商家接单中","fromId":30202,"msgTime":"2021-06-03 17:42:05","order":{"id":108,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106031742405712},"type":21},{"content":"订单已送达，赶快来评价！","fromId":30202,"msgTime":"2021-06-02 17:38:49","order":{"id":90,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106021011188923},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-02 10:15:52","order":{"id":90,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106021011188923},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-02 10:15:42","order":{"id":90,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106021011188923},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-01 17:32:47","order":{"id":89,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106011732259042},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-01 17:32:39","order":{"id":89,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106011732259042},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-01 17:30:27","order":{"id":72,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2105311615807838},"type":21},{"content":"商家备货完成等待支付","fromId":30202,"msgTime":"2021-06-01 16:18:50","order":{"id":79,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106011441438869},"type":21},{"content":"商家已接单，正在备货","fromId":30202,"msgTime":"2021-06-01 16:18:43","order":{"id":79,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106011441438869},"type":21},{"content":"您有订单被商家拒绝，拒绝原因：\u201c不可抗力因素原因\u201d，是否选择随 机发送商家？","fromId":30202,"msgTime":"2021-06-01 15:58:48","order":{"id":79,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106011441438869},"type":21},{"content":"您有订单被商家拒绝，拒绝原因：\u201c距离过远无法配送\u201d，是否选择随 机发送商家？","fromId":30202,"msgTime":"2021-06-01 15:38:26","order":{"id":80,"storeAvatar":"","storeId":"","storeName":"","tradeNo":2106011447443792},"type":21}]
         * total : 0
         */

        private int total;
        private List<MessageBean> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<MessageBean> getList() {
            return list;
        }

        public void setList(List<MessageBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * content : 订单支付超时，已为您自动取消
             * fromId : 30202
             * msgTime : 2021-06-07 09:20:02
             * order : {"id":146,"storeAvatar":"http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png","storeId":1,"storeName":"Geiger","tradeNo":2106041511926921}
             * type : 21
             */

            private String content;
            private int fromId;
            private String msgTime;
            private OrderBean order;
            private int type;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getFromId() {
                return fromId;
            }

            public void setFromId(int fromId) {
                this.fromId = fromId;
            }

            public String getMsgTime() {
                return msgTime;
            }

            public void setMsgTime(String msgTime) {
                this.msgTime = msgTime;
            }

            public OrderBean getOrder() {
                return order;
            }

            public void setOrder(OrderBean order) {
                this.order = order;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public static class OrderBean {
                /**
                 * id : 146
                 * storeAvatar : http://oss.dgkjmm.com/upload/2021-05-18 15:08:13/c356f5e18ac24f9a9fb1307f8bb22356.png
                 * storeId : 1
                 * storeName : Geiger
                 * tradeNo : 2106041511926921
                 */

                private int id;
                private String storeAvatar;
                private String storeId;
                private String storeName;
                private long tradeNo;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getStoreAvatar() {
                    return storeAvatar;
                }

                public void setStoreAvatar(String storeAvatar) {
                    this.storeAvatar = storeAvatar;
                }

                public String getStoreId() {
                    return storeId;
                }

                public void setStoreId(String storeId) {
                    this.storeId = storeId;
                }

                public String getStoreName() {
                    return storeName;
                }

                public void setStoreName(String storeName) {
                    this.storeName = storeName;
                }

                public long getTradeNo() {
                    return tradeNo;
                }

                public void setTradeNo(long tradeNo) {
                    this.tradeNo = tradeNo;
                }
            }
        }
    }

    public static class SubscribeBean {
        /**
         * fromAvatar : https://www.gxdingo.com/static/images/app/icon_delivery1088.png
         * fromId : 30202
         * fromName : 交易物流
         * fromType : 30
         * id : 112
         * toAvatar : http://oss.dgkjmm.com/upload/2021-05-12 16:20:08/b5a233e8fa414d8d9d2184edd2bdabc4.jpeg
         * toId : 10708
         * toName : 巴扎黑
         */

        private String fromAvatar;
        private int fromId;
        private String fromName;
        private int fromType;
        private int id;
        private String toAvatar;
        private int toId;
        private String toName;

        public String getFromAvatar() {
            return fromAvatar;
        }

        public void setFromAvatar(String fromAvatar) {
            this.fromAvatar = fromAvatar;
        }

        public int getFromId() {
            return fromId;
        }

        public void setFromId(int fromId) {
            this.fromId = fromId;
        }

        public String getFromName() {
            return fromName;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public int getFromType() {
            return fromType;
        }

        public void setFromType(int fromType) {
            this.fromType = fromType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getToAvatar() {
            return toAvatar;
        }

        public void setToAvatar(String toAvatar) {
            this.toAvatar = toAvatar;
        }

        public int getToId() {
            return toId;
        }

        public void setToId(int toId) {
            this.toId = toId;
        }

        public String getToName() {
            return toName;
        }

        public void setToName(String toName) {
            this.toName = toName;
        }
    }
}
