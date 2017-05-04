package com.xkj.binaryoption.bean;

/**
 * Created by huangsc on 2017-05-04.
 * TODO:管理页面
 */

public class BeanManages {
    String desc1;
    int imageRe1;
    String desc2;
    int imageRe2;

    public BeanManages(String desc1, int imageRe1, String desc2, int imageRe2) {
        this.desc1 = desc1;
        this.imageRe1 = imageRe1;
        this.desc2 = desc2;
        this.imageRe2 = imageRe2;
    }

    public BeanManages(String desc1, int imageRe1) {
        this.desc1 = desc1;
        this.imageRe1 = imageRe1;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public int getImageRe1() {
        return imageRe1;
    }

    public void setImageRe1(int imageRe1) {
        this.imageRe1 = imageRe1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public int getImageRe2() {
        return imageRe2;
    }

    public void setImageRe2(int imageRe2) {
        this.imageRe2 = imageRe2;
    }
}

