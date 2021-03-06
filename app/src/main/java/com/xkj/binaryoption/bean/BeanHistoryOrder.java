package com.xkj.binaryoption.bean;

import java.util.List;

/**
 * 历史订单记录
 */
public class BeanHistoryOrder {

    /**
     * count : 100
     * items : [{"close_price":"0.85061","close_time":"2017-02-21 15:25:15","commision_level":85,"direction":1,"money":10,"open_price":"0.85055","open_time":"2017-02-21 15:24:15","result":2,"symbol":"EURGBPbo","ticket":4524411,"time_span":60}]
     * msg_type : 290
     */

    private int count;
    private int msg_type;
    private List<ItemsBean> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * close_price : 0.85061
         * close_time : 2017-02-21 15:25:15
         * commision_level : 85
         * direction : 1
         * money : 10
         * open_price : 0.85055
         * open_time : 2017-02-21 15:24:15
         * result : 2
         * symbol : EURGBPbo
         * ticket : 4524411
         * time_span : 60
         */

        private String close_price;
        private String close_time;
        private int commision_level;
        private int direction;
        private int money;
        private String open_price;
        private String open_time;
        private int result;
        private String symbol;
        private int ticket;
        private int time_span;

        public String getClose_price() {
            return close_price;
        }

        public void setClose_price(String close_price) {
            this.close_price = close_price;
        }

        public String getClose_time() {
            return close_time;
        }

        public void setClose_time(String close_time) {
            this.close_time = close_time;
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

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
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
