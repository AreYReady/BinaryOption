package com.xkj.binaryoption.io.okhttp;

import android.util.Log;

import com.xkj.binaryoption.utils.SystemUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

/**
 * Created by huangsc on 2017-02-23.
 * TODO:
 */

public abstract  class MyCallBack implements Callback {
    private String TAG= SystemUtil.getTAG(this);
    @Override
    public void onFailure(Call call, IOException e) {
        Log.i(TAG, "onFailure: "+call.request());
    }
}
