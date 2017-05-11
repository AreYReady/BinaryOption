package com.xkj.binaryoption.mvp.login.p;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.xkj.binaryoption.BuildConfig;
import com.xkj.binaryoption.bean.BeanCrUserList;
import com.xkj.binaryoption.bean.BeanCurrentServerTime;
import com.xkj.binaryoption.bean.BeanServerTimeForHttp;
import com.xkj.binaryoption.bean.BeanUserLoginLogin;
import com.xkj.binaryoption.constant.MyConstant;
import com.xkj.binaryoption.constant.RequestConstant;
import com.xkj.binaryoption.constant.ServerIP;
import com.xkj.binaryoption.constant.UrlConstant;
import com.xkj.binaryoption.handler.HandlerSend;
import com.xkj.binaryoption.handler.HandlerWrite;
import com.xkj.binaryoption.io.okhttp.OkhttpUtils;
import com.xkj.binaryoption.mvp.login.LoginPrestener;
import com.xkj.binaryoption.utils.ACache;
import com.xkj.binaryoption.utils.AesEncryptionUtil;
import com.xkj.binaryoption.utils.DateUtils;
import com.xkj.binaryoption.utils.SSLSOCKET.Decoder;
import com.xkj.binaryoption.utils.SSLSOCKET.Encoder;
import com.xkj.binaryoption.utils.SSLSOCKET.SSLDecoderImp;
import com.xkj.binaryoption.utils.SSLSOCKET.SSLEncodeImp;
import com.xkj.binaryoption.utils.SSLSOCKET.SSLSocketChannel;
import com.xkj.binaryoption.utils.SystemUtil;
import com.xkj.binaryoption.utils.ThreadHelper;

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
    LoginPrestener.ViewListener mLoginView;
    HandlerThread mHandlerThread;
    private SSLSocketChannel<String> mSSLSocketChannel;
    private HandlerWrite mHandlerWrite;
    private Context mContext;
    public LoginPresenterCompl(LoginPrestener.ViewListener iLoginView, Context context) {
        this.mLoginView = iLoginView;
        mContext=context;
    }

    @Override
    public void doLogin( String name, String passwd) {
        loginHttp(name,passwd);
//        loginSocket(name, passwd);
//
    }

    private void loginHttp(String name,String passwd) {
        if(BeanCurrentServerTime.getInstance().isServerTime()){
            sendLoginHttpRequest(name,passwd);
        }else{
            sendServiceTime(name,passwd);
        }
    }

    private void sendServiceTime(final String name,final  String passwd) {
        OkhttpUtils.enqueue(UrlConstant.URL_SERVICE_TIME, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLoginView.onLoginHttpfaild("网络连接失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanServerTimeForHttp beanServerTimeForHttp = new Gson().fromJson(response.body().string(), BeanServerTimeForHttp.class);
                if (beanServerTimeForHttp.getStatus() == 1) {
                    BeanCurrentServerTime.getInstance().setServerTime(DateUtils.getOrderStartTimeNoTimeZone(beanServerTimeForHttp.getData(), "yyyyMMddHHmmss"));
                        loginHttp(name,passwd);
                }
            }
        });
    }

    private void sendLoginHttpRequest(final String name, final String passwd) {
        Map<String ,String> map=new TreeMap<>();
        map.put(RequestConstant.PHONE, AesEncryptionUtil.stringBase64toString(name));
        OkhttpUtils.enqueue(UrlConstant.URL_MT4_CRMUSERLIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLoginView.onLoginHttpfaild("网络连接失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanCrUserList beanCrUserList=new Gson().fromJson(response.body().string(),BeanCrUserList.class);
                Log.i(TAG, "onResponse: 查询mt4账号列表"+new Gson().toJson(beanCrUserList));
                if(beanCrUserList.getStatus()==1){
                    if(beanCrUserList.getData()==null||beanCrUserList.getData().get(0).getMt4lists()==null){
                        mLoginView.onLoginHttpfaild("该手机号未注册账号");
                    }else{
                        loginSocket(beanCrUserList.getData().get(0).getMt4lists().get(0).getLogin(),passwd);
                    }
                }else{
                    mLoginView .onLoginHttpfaild(beanCrUserList.getMsg());
                }
            }
        });
    }

    private void loginSocket(final String name, final String passwd) {
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ACache.get(mContext).put(MyConstant.user_name_mt4,name);
                mHandlerThread = new HandlerThread("SSL"){
                    @Override
                    public void run() {
                        try {
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
        });
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
    }

}
