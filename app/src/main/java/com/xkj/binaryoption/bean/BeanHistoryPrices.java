package com.xkj.binaryoption.bean;

import java.util.List;

/**
 * Created by huangsc on 2017-04-27.
 * TODO:历史报价
 */

public class BeanHistoryPrices {

    /**
     * count : 60
     * digits : 2
     * items : [{"o":"126972|28|-14|-14","t":1493267100,"v":158},{"o":"126947|14|-55|-46","t":300,"v":145},{"o":"126902|19|-1|19","t":600,"v":96},{"o":"126910|11|-11|5","t":900,"v":53},{"o":"126917|90|-7|60","t":1200,"v":257},{"o":"126988|5|-52|-22","t":1500,"v":173},{"o":"126964|0|-52|-52","t":1800,"v":115},{"o":"126917|59|-22|12","t":5700,"v":275},{"o":"126916|33|-12|17","t":6000,"v":238},{"o":"126948|32|-30|-12","t":6300,"v":260},{"o":"126948|52|-112|-111","t":6600,"v":459},{"o":"126854|22|-52|2","t":6900,"v":402},{"o":"126870|7|-62|-56","t":7200,"v":244},{"o":"126829|38|-26|23","t":7500,"v":259},{"o":"126867|10|-25|0","t":7800,"v":141},{"o":"126854|54|0|12","t":8100,"v":89},{"o":"126874|14|-28|-23","t":8400,"v":62},{"o":"126848|25|-12|25","t":8700,"v":31},{"o":"126883|20|-34|-19","t":9000,"v":53},{"o":"126867|8|-14|-14","t":9300,"v":44},{"o":"126852|29|-48|-27","t":9600,"v":46},{"o":"126835|7|-37|-10","t":9900,"v":100},{"o":"126822|0|-40|-31","t":10200,"v":82},{"o":"126793|42|-11|38","t":10500,"v":145},{"o":"126834|24|-37|16","t":10800,"v":134},{"o":"126874|8|-39|-38","t":11100,"v":318},{"o":"126850|8|-58|-39","t":11400,"v":195},{"o":"126799|12|-37|-20","t":11700,"v":145},{"o":"126769|40|-4|23","t":12000,"v":257},{"o":"126790|14|-24|-24","t":12300,"v":57},{"o":"126772|19|-19|14","t":12600,"v":66},{"o":"126792|22|-66|-47","t":12900,"v":149},{"o":"126744|3|-22|-20","t":13200,"v":180},{"o":"126727|14|-37|0","t":13500,"v":204},{"o":"126748|16|-32|-22","t":13800,"v":57},{"o":"126727|32|-20|29","t":14100,"v":108},{"o":"126757|27|-2|15","t":14400,"v":109},{"o":"126774|32|-11|-9","t":14700,"v":85},{"o":"126755|40|-5|28","t":15000,"v":95},{"o":"126784|10|-18|-7","t":15300,"v":91},{"o":"126775|79|0|47","t":15600,"v":118},{"o":"126826|17|-24|-11","t":15900,"v":152},{"o":"126816|57|-1|45","t":16200,"v":119},{"o":"126855|5|-222|-179","t":16500,"v":423},{"o":"126678|18|-24|-12","t":16800,"v":260},{"o":"126668|29|-41|-1","t":17100,"v":217},{"o":"126666|43|-9|24","t":17400,"v":201},{"o":"126691|6|-28|-8","t":17700,"v":156},{"o":"126684|10|-27|-26","t":18000,"v":108},{"o":"126657|48|-12|20","t":18300,"v":184},{"o":"126675|33|-7|3","t":18600,"v":148},{"o":"126677|0|-48|-35","t":18900,"v":157},{"o":"126645|14|-18|-6","t":19200,"v":207},{"o":"126642|76|-2|34","t":19500,"v":156},{"o":"126677|35|-27|2","t":19800,"v":197},{"o":"126678|43|-4|21","t":20100,"v":205},{"o":"126698|4|-28|-6","t":20400,"v":86},{"o":"126691|23|-10|10","t":20700,"v":152},{"o":"126702|29|-11|-3","t":21000,"v":131},{"o":"126696|6|-6|6","t":21300,"v":13}]
     * msg_type : 1101
     * period : 5
     * result_code : 0
     * symbol : XAUUSDbo
     */

    private int count;
    private int digits;
    private int msg_type;
    private int period;
    private int result_code;
    private String symbol;
    private List<ItemsBean> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDigits() {
        return digits;
    }

    public void setDigits(int digits) {
        this.digits = digits;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * o : 126972|28|-14|-14
         * t : 1493267100
         * v : 158
         */

        private String o;
        private int t;
        private int v;

        public String getO() {
            return o;
        }

        public void setO(String o) {
            this.o = o;
        }

        public int getT() {
            return t;
        }

        public void setT(int t) {
            this.t = t;
        }

        public int getV() {
            return v;
        }

        public void setV(int v) {
            this.v = v;
        }
    }
}
