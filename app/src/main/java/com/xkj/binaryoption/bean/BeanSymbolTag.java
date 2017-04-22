package com.xkj.binaryoption.bean;

/**
 * Created by huangsc on 2017-04-19.
 * TODO:
 */

public class BeanSymbolTag {
    String desc;
    String symbol;
    String amount;
    /**
     * 涨落，ture为涨,false 落
     */
    Boolean upOrDown;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BeanSymbolTag() {
    }

    public BeanSymbolTag(String desc, String symbol, String amount, Boolean upOrDown) {
        this.desc = desc;
        this.symbol = symbol;
        this.amount = amount;
        this.upOrDown = upOrDown;
    }

    public String getSymbol() {

        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Boolean getUpOrDown() {
        return upOrDown;
    }

    public void setUpOrDown(Boolean upOrDown) {
        this.upOrDown = upOrDown;
    }
}
