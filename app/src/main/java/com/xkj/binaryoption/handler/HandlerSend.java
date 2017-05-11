package com.xkj.binaryoption.handler;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.xkj.binaryoption.utils.SSLSOCKET.SSLSocketChannel;
import com.xkj.binaryoption.utils.SocketUtil;
import com.xkj.binaryoption.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by jimdaxu on 16/9/6.
 * 发送子线程的handler
 */
public class HandlerSend extends Handler {
    public final static int CONNECT = 1;
    public final static int CLOSE = 2;
    private String TAG= SystemUtil.getTAG(this);
    private Context mContext;
    private HandlerThread mHandlerThread;
    private SSLSocketChannel mSSLSocketChannel;
    private HandlerWrite mHandlerWrite;
    private HashSet<String> mSubSymbolsSet=new HashSet<>();
    public HandlerSend(Looper looper, Context context
            , HandlerThread handlerThread, SSLSocketChannel SSLSocketChannel
            , HandlerWrite handlerWrite) {
        super(looper);
        mContext = context;
        mHandlerThread = handlerThread;
        mSSLSocketChannel = SSLSocketChannel;
        mHandlerWrite = handlerWrite;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case CONNECT://连接
                try {
                    if (mSSLSocketChannel != null){
                        mSSLSocketChannel.close();
                        mSSLSocketChannel=null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    mSSLSocketChannel = SocketUtil.connectToServer(mContext, mHandlerThread, mHandlerWrite);
                    EventBus.getDefault().postSticky(this);
                    //将之前收集的订阅信息重新发送
//                    if(mSubSymbolsSet.size()!=0){
//                        for(String subSymbol:mSubSymbolsSet){
//                            if (mSSLSocketChannel != null) {
//                                Message message=new Message();
//                                message.obj=subSymbol;
//                                this.sendMessage(message);
//                            }
//                        }
//                    }
                }
                break;
            case CLOSE://关闭
                try {
                    if (mSSLSocketChannel != null){
                        mSSLSocketChannel.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    mSSLSocketChannel = null;
                    Log.i(TAG, "handleMessage: mSSLSocketChannel为空");
//                    EventBus.getDefault().post(LoginPresenterCompl.DISCONNECT_FROM_SERVER);
                }
                break;
            default:
                String data = (String) msg.obj;
//                //收集订阅列表
//                if(mSubSymbolsSet!=null&&data.contains("1010")){
//                    mSubSymbolsSet.add(data);
//                }
                if (mSSLSocketChannel!=null)
                mSSLSocketChannel.send(data);
                Log.i(TAG, "handleMessage: "+data);
                break;
        }
    }

    public SSLSocketChannel getSSLSocketChannel() {
        return mSSLSocketChannel;
    }

}
