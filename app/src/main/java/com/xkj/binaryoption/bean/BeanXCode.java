package com.xkj.binaryoption.bean;

/**
 * Created by huangsc on 2017-05-10.
 * TODO:验证码
 */

public class BeanXCode {
    /**
     * status : 1
     * msg : success
     * data : {"xcode":"2963"}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * xcode : 2963
         */

        private String xcode;

        public String getXcode() {
            return xcode;
        }

        public void setXcode(String xcode) {
            this.xcode = xcode;
        }
    }

}
