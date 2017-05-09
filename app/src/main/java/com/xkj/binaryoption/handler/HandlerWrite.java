package com.xkj.binaryoption.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.xkj.binaryoption.message.MessageDisconnect;
import com.xkj.binaryoption.utils.SSLSOCKET.SSLSocketChannel;
import com.xkj.binaryoption.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by jimdaxu on 16/9/6.
 * 读取服务器响应
 */
public class HandlerWrite extends Handler {
    private String TAG= SystemUtil.getTAG(this);
    private SSLSocketChannel<String> mSSLSocketChannel;

    public HandlerWrite(Looper looper, SSLSocketChannel<String> SSLSocketChannel) {
        super(looper);
        mSSLSocketChannel = SSLSocketChannel;
    }

    public void setSSLSocketChannel(SSLSocketChannel<String> SSLSocketChannel) {
        mSSLSocketChannel = SSLSocketChannel;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        String response = null;
        boolean flag = true;
        try {
            while (flag&&mSSLSocketChannel!=null){
                response = mSSLSocketChannel.receive();
                Log.i(TAG, "doLogin: Response received: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                mSSLSocketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }finally {
                mSSLSocketChannel = null;
                //如果锻炼，重新发送
                EventBus.getDefault().post(new MessageDisconnect());
                flag = false;
                Log.i(TAG, "doLogin: Response received: " + response);
            }
        }
        Log.i(TAG, "doLogin: Response received: " + response);
    }
}
