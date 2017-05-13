package com.xkj.binaryoption.bean;

/**
 * Created by huangsc on 2017-05-12.
 * TODO:密码重置类
 */

public class BeanPassword {

    /**
     * status : 1
     * msg : success
     * data : {"login":"88005894","newpsword":"huang1"}
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
         * login : 88005894
         * newpsword : huang1
         */

        private String login;
        private String newpsword;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getNewpsword() {
            return newpsword;
        }

        public void setNewpsword(String newpsword) {
            this.newpsword = newpsword;
        }
    }
}
