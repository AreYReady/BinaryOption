package com.xkj.binaryoption.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xkj.binaryoption.message.MessageDisconnect;
import com.xkj.binaryoption.message.MessageHeart;
import com.xkj.binaryoption.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.xkj.binaryoption.handler.HandlerSend.CONNECT;

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

    @Subscribe()
    public void getDisConnectMessage(MessageDisconnect messageDisconnect){
        Log.i(TAG, "getDisConnectMessage: 断连重新连接");
        mHandler.sendEmptyMessage(CONNECT);
    }
    @Subscribe
    public void getHeart(MessageHeart messageHeart){
        Log.i(TAG, "getHeart: "+"");
        sendMessageToServer("{\"msg_type\":2}");
    }
    private void sendMessageToServer(String data) {
        Message message = new Message();
        message.obj = (data);
        mHandler.sendMessage(message);
    }
}
