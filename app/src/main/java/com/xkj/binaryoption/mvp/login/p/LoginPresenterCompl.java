package com.xkj.binaryoption.mvp.login.p;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.xkj.binaryoption.BuildConfig;
import com.xkj.binaryoption.bean.BeanCurrentServerTime;
import com.xkj.binaryoption.bean.BeanServerTimeForHttp;
import com.xkj.binaryoption.bean.BeanUserLoginLogin;
import com.xkj.binaryoption.bean.IUserLogin;
import com.xkj.binaryoption.constant.RequestConstant;
import com.xkj.binaryoption.constant.ServerIP;
import com.xkj.binaryoption.constant.UrlConstant;
import com.xkj.binaryoption.handler.HandlerSend;
import com.xkj.binaryoption.handler.HandlerWrite;
import com.xkj.binaryoption.io.okhttp.OkhttpUtils;
import com.xkj.binaryoption.mvp.login.LoginPrestener;
import com.xkj.binaryoption.utils.AesEncryptionUtil;
import com.xkj.binaryoption.utils.DateUtils;
import com.xkj.binaryoption.utils.SSLSOCKET.Decoder;
import com.xkj.binaryoption.utils.SSLSOCKET.Encoder;
import com.xkj.binaryoption.utils.SSLSOCKET.SSLDecoderImp;
import com.xkj.binaryoption.utils.SSLSOCKET.SSLEncodeImp;
import com.xkj.binaryoption.utils.SSLSOCKET.SSLSocketChannel;
import com.xkj.binaryoption.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by huangsc on 2017-04-18.
 * TODO:
 */

public class LoginPresenterCompl implements LoginPrestener.PreListener{
    public static final String DISCONNECT_FROM_SERVER = "DISCONNECT_FROM_SERVER";
    public static final String THREAD_READ = "threadRead";
    public static final String SSL_SOCKET = "sslSocket";
    public static final String HANDLER_WRITE = "handler_write";
    private String TAG= SystemUtil.getTAG(this);
    LoginPrestener.ViewListener iLoginView;
    IUserLogin mIUserLogin;
    HandlerThread mHandlerThread;
    private SSLSocketChannel<String> mSSLSocketChannel;
    private HandlerWrite mHandlerWrite;
    private Context mContext;
    public LoginPresenterCompl(LoginPrestener.ViewListener iLoginView, Context context) {
        this.iLoginView = iLoginView;
        mContext=context;
    }

    @Override
    public void doLogin(final String name, final String passwd) {
        loginHttp(name);
        loginSocket(name, passwd);
//
    }

    private void loginHttp(String name) {
        if(BeanCurrentServerTime.getInstance().isServerTime()){
            sendLoginHttpRequest(name);
        }else{
            sendServiceTime(name);
        }
    }

    private void sendServiceTime(final String name) {
        OkhttpUtils.enqueue(UrlConstant.URL_SERVICE_TIME, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mIUserLogin.onLoginHttpfaild("网络连接失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanServerTimeForHttp beanServerTimeForHttp = new Gson().fromJson(response.body().string(), BeanServerTimeForHttp.class);
                if (beanServerTimeForHttp.getStatus() == 1) {
                    BeanCurrentServerTime.getInstance().setServerTime(DateUtils.getOrderStartTimeNoTimeZone(beanServerTimeForHttp.getData(), "yyyyMMddHHmmss"));
                        loginHttp(name);
                }
            }
        });
    }

    private void sendLoginHttpRequest(String name) {
        Map<String ,String> map=new TreeMap<>();
        map.put(RequestConstant.PHONE, AesEncryptionUtil.stringBase64toString(name));
        OkhttpUtils.enqueue(UrlConstant.URL_MT4_CRMUSERLIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mIUserLogin.onLoginHttpfaild("网络连接失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: 查询mt4账号列表"+response.body().string());
            }
        });
    }

    private void loginSocket(final String name, final String passwd) {
        mHandlerThread = new HandlerThread("SSL"){
            @Override
            public void run() {
                try {
                    Log.i("123", "run: sslTest");
                    sslTest(name, passwd);
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };
        mHandlerThread.start();
        Handler handlerRead = new HandlerSend(mHandlerThread.getLooper(),
                mContext,mHandlerThread, mSSLSocketChannel, mHandlerWrite);
        EventBus.getDefault().postSticky(handlerRead);
    }

    private void sslTest(String name, String passwd) throws KeyManagementException {
        final SocketAddress address = new InetSocketAddress(BuildConfig.API_URL, ServerIP.PORT);
        Encoder<String> encoder = new SSLEncodeImp();
        Decoder<String> decoder = new SSLDecoderImp();
        Log.i("123", "doLogin: Opening channel");
        try {
            mSSLSocketChannel = SSLSocketChannel.open(address, encoder, decoder, 1024*1024, 1024*1024);
            HandlerThread writeThread = new HandlerThread("write");
            writeThread.start();
            mHandlerWrite = new HandlerWrite(writeThread.getLooper(), mSSLSocketChannel);
            Log.i("123", "doLogin: Channel opened, initial handshake done");
            Log.i("123", "doLogin: Sending request");
            BeanUserLoginLogin userLogin = new BeanUserLoginLogin(Integer.valueOf(name), passwd);
            String loginStr = new Gson().toJson(userLogin, BeanUserLoginLogin.class);
            mSSLSocketChannel.send(loginStr);
            Log.i("123", "doLogin: Receiving response");
            mHandlerWrite.sendEmptyMessage(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        iLoginView = null;
    }

}
