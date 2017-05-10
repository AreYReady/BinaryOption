package com.xkj.binaryoption.bean;

/**
 * Created by huangsc on 2017-04-26.
 * TODO:注册信息返回
 */

public class BeanSignUpInfo
{
    /**
     * status : 1
     * msg : success
     * data : {"mt4":{"aid":"158","name":"adfa","login":88058119,"password":"bh492zs","check_status":1,"password_investor":"jdncrx3","create_at":"2017-04-26 09:55:33"}}
     */
    //后面增加的类
    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    String tips;
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
         * mt4 : {"aid":"158","name":"adfa","login":88058119,"password":"bh492zs","check_status":1,"password_investor":"jdncrx3","create_at":"2017-04-26 09:55:33"}
         */

        private Mt4Bean mt4;

        public Mt4Bean getMt4() {
            return mt4;
        }

        public void setMt4(Mt4Bean mt4) {
            this.mt4 = mt4;
        }

        public static class Mt4Bean {
            /**
             * aid : 158
             * name : adfa
             * login : 88058119
             * password : bh492zs
             * check_status : 1
             * password_investor : jdncrx3
             * create_at : 2017-04-26 09:55:33
             */

            private String aid;
            private String name;
            private int login;
            private String password;
            private int check_status;
            private String password_investor;
            private String create_at;

            public String getAid() {
                return aid;
            }

            public void setAid(String aid) {
                this.aid = aid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getLogin() {
                return login;
            }

            public void setLogin(int login) {
                this.login = login;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public int getCheck_status() {
                return check_status;
            }

            public void setCheck_status(int check_status) {
                this.check_status = check_status;
            }

            public String getPassword_investor() {
                return password_investor;
            }

            public void setPassword_investor(String password_investor) {
                this.password_investor = password_investor;
            }

            public String getCreate_at() {
                return create_at;
            }

            public void setCreate_at(String create_at) {
                this.create_at = create_at;
            }
        }
    }
}
