package com.xkj.binaryoption.constant;

/**
 * Created by huangsc on 2017-04-24.
 * TODO:
 */

public  class MyConstant {
    /**
     * 按钮时间，买涨买跌
     */
    public static enum BuyAciton{
        BUY_DOWN,
        BUY_UP;
    }
    public static String user_name="Login_telephone";
    public static String user_name_mt4="Login_mt4";
    public static String user_password="password";
    public static String IS_REMEMBER="is_remember";
    public static String  regPh = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
    public static String regPassword = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";



    public static  String Chang_amount="amount";
    public static  String Chang_background="background";
    /**
     * 线图的空闲位置
     */
    public static  int LINK_FREE_SPACE_H=15;
    public static  int LINK_FREE_SPACE_W=7;
}
