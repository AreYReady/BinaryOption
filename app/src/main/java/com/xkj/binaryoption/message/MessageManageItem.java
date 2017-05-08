package com.xkj.binaryoption.message;

/**
 * Created by huangsc on 2017-05-08.
 * TODO:个人中心的item的事件实体类
 */

public class MessageManageItem {
    String itemName;

    public String getItemName() {
        return itemName;
    }

    public MessageManageItem(String itemName) {
        this.itemName = itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
