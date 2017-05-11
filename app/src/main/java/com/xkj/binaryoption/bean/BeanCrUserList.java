package com.xkj.binaryoption.bean;

import java.util.List;

/**
 * Created by huangsc on 2017-05-11.
 * TODO:mt4列表
 */

public class BeanCrUserList {

    /**
     * status : 1
     * msg : success
     * data : [{"name":"huang","phone":"15059790550","email":"","gender":"0","usertype":"0","mt4group":"","sta_use":"1","rate":"6.94","lowest_in":"500","lowest_out":"50","highest_out":"50000","bankcard":null,"mt4lists":[{"login":"88005894","mt4name":"huang","sta_check":"1","createtime":"2017-05-10 18:19:31"}]}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : huang
         * phone : 15059790550
         * email :
         * gender : 0
         * usertype : 0
         * mt4group :
         * sta_use : 1
         * rate : 6.94
         * lowest_in : 500
         * lowest_out : 50
         * highest_out : 50000
         * bankcard : null
         * mt4lists : [{"login":"88005894","mt4name":"huang","sta_check":"1","createtime":"2017-05-10 18:19:31"}]
         */

        private String name;
        private String phone;
        private String email;
        private String gender;
        private String usertype;
        private String mt4group;
        private String sta_use;
        private String rate;
        private String lowest_in;
        private String lowest_out;
        private String highest_out;
        private Object bankcard;
        private List<Mt4listsBean> mt4lists;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }

        public String getMt4group() {
            return mt4group;
        }

        public void setMt4group(String mt4group) {
            this.mt4group = mt4group;
        }

        public String getSta_use() {
            return sta_use;
        }

        public void setSta_use(String sta_use) {
            this.sta_use = sta_use;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getLowest_in() {
            return lowest_in;
        }

        public void setLowest_in(String lowest_in) {
            this.lowest_in = lowest_in;
        }

        public String getLowest_out() {
            return lowest_out;
        }

        public void setLowest_out(String lowest_out) {
            this.lowest_out = lowest_out;
        }

        public String getHighest_out() {
            return highest_out;
        }

        public void setHighest_out(String highest_out) {
            this.highest_out = highest_out;
        }

        public Object getBankcard() {
            return bankcard;
        }

        public void setBankcard(Object bankcard) {
            this.bankcard = bankcard;
        }

        public List<Mt4listsBean> getMt4lists() {
            return mt4lists;
        }

        public void setMt4lists(List<Mt4listsBean> mt4lists) {
            this.mt4lists = mt4lists;
        }

        public static class Mt4listsBean {
            /**
             * login : 88005894
             * mt4name : huang
             * sta_check : 1
             * createtime : 2017-05-10 18:19:31
             */

            private String login;
            private String mt4name;
            private String sta_check;
            private String createtime;

            public String getLogin() {
                return login;
            }

            public void setLogin(String login) {
                this.login = login;
            }

            public String getMt4name() {
                return mt4name;
            }

            public void setMt4name(String mt4name) {
                this.mt4name = mt4name;
            }

            public String getSta_check() {
                return sta_check;
            }

            public void setSta_check(String sta_check) {
                this.sta_check = sta_check;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }
        }
    }
}
