package com.xkj.binaryoption.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xkj.binaryoption.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by huangsc on 2017-04-17.
 * TODO:
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG= SystemUtil.getTAG(this);
    private Handler mHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRegister();
        initView();
        initData();
    }


    /**
     * 注册所有该注册的
     */
    public abstract void initRegister();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
    @Subscribe(sticky = true)
    public void getHandler(Handler handler){
        mHandler=handler;
    }
}
