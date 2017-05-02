package com.xkj.binaryoption.bean;

import java.util.List;

/**
 * Created by huangsc on 2017-05-02.
 * TODO:
 */

public class BeanCurrentOrder {

    /**
     * msg_type : 310
     * ordercount : 1
     * orders : [{"close_price":"1255.19","commision_level":75,"direction":1,"left_time":29561,"money":100,"open_price":"1255.16","open_time":"2017-05-02 17:12:29","symbol":"XAUUSDbo","ticket":4527777,"time_span":120}]
     */

    private int msg_type;
    private int ordercount;
    private List<OrdersBean> orders;

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public int getOrdercount() {
        return ordercount;
    }

    public void setOrdercount(int ordercount) {
        this.ordercount = ordercount;
    }

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public static class OrdersBean {
        /**
         * close_price : 1255.19
         * commision_level : 75
         * direction : 1
         * left_time : 29561
         * money : 100
         * open_price : 1255.16
         * open_time : 2017-05-02 17:12:29
         * symbol : XAUUSDbo
         * ticket : 4527777
         * time_span : 120
         */

        private String close_price;
        private int commision_level;
        private int direction;
        private int left_time;
        private int money;
        private String open_price;
        private String open_time;
        private String symbol;
        private int ticket;
        private int time_span;

        public String getClose_price() {
            return close_price;
        }

        public void setClose_price(String close_price) {
            this.close_price = close_price;
        }

        public int getCommision_level() {
            return commision_level;
        }

        public void setCommision_level(int commision_level) {
            this.commision_level = commision_level;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public int getLeft_time() {
            return left_time;
        }

        public void setLeft_time(int left_time) {
            this.left_time = left_time;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getOpen_price() {
            return open_price;
        }

        public void setOpen_price(String open_price) {
            this.open_price = open_price;
        }

        public String getOpen_time() {
            return open_time;
        }

        public void setOpen_time(String open_time) {
            this.open_time = open_time;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public int getTicket() {
            return ticket;
        }

        public void setTicket(int ticket) {
            this.ticket = ticket;
        }

        public int getTime_span() {
            return time_span;
        }

        public void setTime_span(int time_span) {
            this.time_span = time_span;
        }
    }
}
