package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Kikis
 * @date: 2021/5/19
 * @page:
 */
public class SubscribesBean {

    /**
     * list : [{"fromAvatar":"http://oss.dgkjmm.com/upload/20200730/643fe7fed3d7438eb767c86f75471dce.jpg","fromName":"zhangsan","fromType":10,"id":8,"lastMsg":"没有了","lastMsgTime":"2020-08-26 17:32:29","order":{"id":3541,"storeAvatar":"http://xxxxxxx.com/image.jpg","storeId":35,"storeName":"店铺名称","tradeNo":210235413333},"unreadNum":1},{"fromAvatar":"http://oss.dgkjmm.com/upload/20200730/643fe7fed3d7438eb767c86f75471dce.jpg","fromName":"zhangsan","fromType":20,"id":9,"lastMsg":"没有了","lastMsgTime":"2020-08-26 17:32:29","unreadNum":1}]
     * total : 12
     * totalUnread : 22
     * wsServerUrl : ws://xx.xx.xx.xx/socketio
     */

    private int total;
    private int totalUnread;
    private String wsServerUrl;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalUnread() {
        return totalUnread;
    }

    public void setTotalUnread(int totalUnread) {
        this.totalUnread = totalUnread;
    }

    public String getWsServerUrl() {
        return wsServerUrl;
    }

    public void setWsServerUrl(String wsServerUrl) {
        this.wsServerUrl = wsServerUrl;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * fromAvatar : http://oss.dgkjmm.com/upload/20200730/643fe7fed3d7438eb767c86f75471dce.jpg
         * fromName : zhangsan
         * fromType : 10
         * id : 8
         * lastMsg : 没有了
         * lastMsgTime : 2020-08-26 17:32:29
         * order : {"id":3541,"storeAvatar":"http://xxxxxxx.com/image.jpg","storeId":35,"storeName":"店铺名称","tradeNo":210235413333}
         * unreadNum : 1
         */

        private String fromAvatar;
        private String fromName;
        private int fromType;
        private int id;
        private String lastMsg;
        private String lastMsgTime;
        private OrderBean order;
        private int unreadNum;

        public String getFromAvatar() {
            return fromAvatar;
        }

        public void setFromAvatar(String fromAvatar) {
            this.fromAvatar = fromAvatar;
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

        public String getLastMsg() {
            return lastMsg;
        }

        public void setLastMsg(String lastMsg) {
            this.lastMsg = lastMsg;
        }

        public String getLastMsgTime() {
            return lastMsgTime;
        }

        public void setLastMsgTime(String lastMsgTime) {
            this.lastMsgTime = lastMsgTime;
        }

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public int getUnreadNum() {
            return unreadNum;
        }

        public void setUnreadNum(int unreadNum) {
            this.unreadNum = unreadNum;
        }

        public static class OrderBean {
            /**
             * id : 3541
             * storeAvatar : http://xxxxxxx.com/image.jpg
             * storeId : 35
             * storeName : 店铺名称
             * tradeNo : 210235413333
             */

            private int id;
            private String storeAvatar;
            private int storeId;
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

            public int getStoreId() {
                return storeId;
            }

            public void setStoreId(int storeId) {
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
