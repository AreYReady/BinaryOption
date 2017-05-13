package com.xkj.binaryoption.base;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by admin on 2016-11-17.
 */

public class MyApplication extends Application {
    private static MyApplication instance;


    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }

    private List<Activity> mList = new LinkedList();

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {

            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    public int getListSize(){
            return mList.size();
    }

    public void deleteActivity(Activity activity) {
        if (activityExist(activity)) {
            mList.remove(activity);
        }
    }

    private boolean activityExist(Activity activity) {
        for (Activity a : mList) {
            if (a == activity) {
                return true;
            }
        }
        return false;
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
