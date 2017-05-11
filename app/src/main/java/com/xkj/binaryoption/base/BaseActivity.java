package com.xkj.binaryoption.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.xkj.binaryoption.R;
import com.xkj.binaryoption.message.MessageDisconnect;
import com.xkj.binaryoption.message.MessageHeart;
import com.xkj.binaryoption.utils.SSLSOCKET.SSLSocketChannel;
import com.xkj.binaryoption.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.xiasuhuei321.loadingdialog.view.LoadingDialog.Speed.SPEED_TWO;
import static com.xkj.binaryoption.handler.HandlerSend.CONNECT;

/**
 * Created by huangsc on 2017-04-17.
 * TODO:
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG= SystemUtil.getTAG(this);
    protected Context mContext;
    private Handler mHandler;
    private int connectCount=10;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
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
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void getHandler(Handler handler){
        mHandler=handler;
        if(loadingDialog!=null){
            loadingDialog.close();
            loadingDialog=null;
        }
    }
    AlertDialog.Builder alertDialog;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDisConnectMessage(MessageDisconnect messageDisconnect){

        Log.i(TAG, "getDisConnectMessage: 断连重新连接");
        if(connectCount>0) {
            connectCount--;
            mHandler.sendEmptyMessage(CONNECT);
        }
//            onTimeOut(SSLSocketChannel.TIMEOUT);
//            showFailedDialog("连接状态","连接断开，请重连");
//        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimeOut(String timeout) {
        if (timeout.equalsIgnoreCase(SSLSocketChannel.TIMEOUT)) {
           showFailedDialog("连接状态","连接超时，请重连");
        }
    }
    private void showFailedDialog(String title,String message){
        if(loadingDialog!=null){
            loadingDialog.close();
            loadingDialog=null;
        }
        alertDialog = new AlertDialog.Builder(mContext, R.style.AlertDialog_Succ).setTitle(title).setCancelable(false).setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                connect();
                mHandler.sendEmptyMessage(CONNECT);
            }
        });
        alertDialog.show();
    }
    LoadingDialog loadingDialog;
    private void connect(){
        if(loadingDialog==null) {
            loadingDialog = new LoadingDialog(mContext);
            loadingDialog.setLoadingText("加载中")
                    //显示加载成功时的文字
                    .setInterceptBack(true)
                    .setLoadSpeed(SPEED_TWO)
                    .show();
        }
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
