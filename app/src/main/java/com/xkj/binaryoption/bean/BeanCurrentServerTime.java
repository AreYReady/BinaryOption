package com.xkj.binaryoption.bean;

import android.util.Log;

import com.xkj.binaryoption.utils.SystemUtil;


/**
 * Created by admin on 2016-11-11.
 */

public class BeanCurrentServerTime {
        long serverTime=0;
        long nativeTime;
        long currentNativeTime;
        public  static BeanCurrentServerTime instance;
        public static  synchronized  BeanCurrentServerTime getInstance(){
            if(instance==null){
                instance=new BeanCurrentServerTime();
            }
            return instance;
        }
        public void setServerTime(long currentServerTime){
            this.nativeTime= System.currentTimeMillis();
            this.serverTime=currentServerTime;
        }

    /**
         * 计算服务器当前的时间
         * @return
         */
        public long getCurrentServerTime(){
            currentNativeTime= System.currentTimeMillis();
            Log.i(SystemUtil.getTAG(this.getClass()), "currentNativeTime: "+currentNativeTime);
            return serverTime+(currentNativeTime - nativeTime);
        }
        public long getOldServerTime(){
            return serverTime;
        }
        public boolean isServerTime(){
         return serverTime==0?false:true;
         }


}
