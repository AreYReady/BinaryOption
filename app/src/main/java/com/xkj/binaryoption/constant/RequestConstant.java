package com.xkj.binaryoption.constant;

/**
 * @author huangsc
 * @date 2016-12-24
 * TODO:接口参数的key
 */
public interface RequestConstant {
    //账号
    String ACCOUNT = "account";
    //mt4账号
    String LOGIN = "login";

    String API_ID = "apiid";
    //服务器时间
    String API_TIME = "apitime";
    //mt4账号密码
    String LOGIN_PASSWORD = "password";
    //标志
    String API_SIGN = "apisign";
    //交易名
    String SYMBOL = "symbol";
    //时间段
    String PERIOD = "period";
    //数量
    String BARNUM = "barnum";
    //是否分页
    String IS_PAGE = "ispage";
    //每页数量
    String PAGE_LIMIT = "pagelimit";

    String RANK_TYPE = "rankType";

    String TYPE = "type";
    //操作动作
    String ACTION = "action";
    String EXC = "exc";
    String VOLUME = "volume";
    String PRICE = "price";
    String SL = "sl";
    String TP = "tp";
    String ORDERNO = "orderno";
    String IS_FILTER = "isfilter";
    String OPEN_START_TIME = "openstarttime";
    String OPEN_END_TIME = "openendtime";
    String CLOSE_START_TIME = "closestarttime";
    String CLOSE_END_TIME = "closeendtime";
    String MASTER_ID = "masterid";
    String FOLLOW_ID = "followid";
    String COPY_WAY = "copyway";
    String COPY_MONEY = "copymoney";
    String COPY_VOLUME = "copyvolume";
    String ISMIN_COPY = "ismin_copy";
    String IS_REVERSE = "is_reverse";
    String DATE_RANGE = "daterange";
    /**
     * 关注人账号
     */
    String ACCOUNT_ID = "accountid";
    /**
     * 被关注人账号
     */
    String FOCUS_ID = "focusid";
    String NAME="name";
    String PHONE="phone";
    String EMAIL="email";
    String xtext="xtext";
    String LEVERAGE="leverage";
    String API_TIPS ="apitips";




     enum Exc {
         BUY("BUY"),//买
         SELL("SELL"),//卖
         BUY_LIMIT("BUYLIMIT"),//买限制
         SELL_LIMIT("SELLLIMIT"),//卖限制
         BUY_STOP("BUYSTOP"),//买止损
         SELL_STOP("SELLSTOP");//买止损
        private final String text;

         Exc(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
    enum Action {
        MAKET("maket"),//开市场单
        PENDING ("pending"),//挂单交易
        CLOSE("closeDialog"),//③平仓交易
        DELETE("delete"),//删除取消
        EDIT("edit");//订单修改
        private final String text;

        Action(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    String PROFIT="profit";
    String CURRENT_PRICE="CURRENT_PRICE";
    String STOP_LOSS="STOP_loss";
    String TAKE_PROFIT="take_profit";
}
