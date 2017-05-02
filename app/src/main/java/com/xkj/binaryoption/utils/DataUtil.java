package com.xkj.binaryoption.utils;

import android.util.Log;


/**
 * Created by admin on 2016-11-24.
 */

public class DataUtil {


    public static int[] drawLineCount(int digits, double maxPrice, double minPrice) {
        double maxPrices = maxPrice;
        double min = minPrice;
        int i = (int) (maxPrice * Math.pow(10, digits) - minPrice * Math.pow(10, digits));
        int[] data = new int[2];
        int len = String.valueOf(i).length();
        data[1] = 10;
        if (i > 5 * Math.pow(10, len - 1)) {
            data[0] = (int) Math.pow(10, len - 1);

        } else if (i > 2 * Math.pow(10, len - 1)) {
            if(len==1){
                data[0]=1;
            }else
            data[0] = (int) Math.pow(10, len - 2) * 5;
        } else {
            data[0] = (int) Math.pow(10, len - 2) * 2;
        }
        if(data[0]==0){
            Log.i("hsc", "drawLineCount: ");
        }
        return data;
    }

    public static int selectPeriod(String period) {
        int mPeriod = 1;
        switch (period) {
            case "m1":
                mPeriod = 1;
                break;
            case "m5":
                mPeriod = 5;
                break;
            case "m15":
                mPeriod = 15;
                break;
            case "m30":
                mPeriod = 30;
                break;
            case "h1":
                mPeriod = 60;
                break;
            case "h4":
                mPeriod = 240;
                break;
            case "d1":
                mPeriod = 1440;
                break;
            case "w1":
                mPeriod = 10080;
                break;
        }
        return mPeriod;
    }

    public static String selectPeriod(int period) {
        String mPeriod = "m1";
        switch (period) {
            case 1:
                mPeriod = "m1";
                break;
            case 5:
                mPeriod = "m5";
                break;
            case 15:
                mPeriod = "m15";
                break;
            case 30:
                mPeriod = "m30";
                break;
            case 60:
                mPeriod = "h1";
                break;
            case 240:
                mPeriod = "h4";
                break;
            case 1440:
                mPeriod = "d1";
                break;
            case 10080:
                mPeriod = "w1";
                break;
        }
        return mPeriod;
    }


    public static String symbolConnectPeriod(String symbol,int period){
        return symbol.concat("_"+period);
    }



}
