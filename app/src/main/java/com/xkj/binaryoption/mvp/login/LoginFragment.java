package com.xkj.binaryoption.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.bean.BeanSymbolConfig;
import com.xkj.binaryoption.bean.ResponseEvent;
import com.xkj.binaryoption.constant.MyConstant;
import com.xkj.binaryoption.message.MessageLostPassword;
import com.xkj.binaryoption.message.MessageSignUp;
import com.xkj.binaryoption.mvp.login.p.LoginPresenterCompl;
import com.xkj.binaryoption.mvp.trade.TradeActivity;
import com.xkj.binaryoption.utils.ACache;
import com.xkj.binaryoption.utils.AesEncryptionUtil;
import com.xkj.binaryoption.utils.CacheUtil;
import com.xkj.binaryoption.utils.SSLSOCKET.SSLSocketChannel;
import com.xkj.binaryoption.utils.SystemUtil;
import com.xkj.binaryoption.utils.ThreadHelper;
import com.xkj.binaryoption.widget.CustomEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by huangsc on 2017-04-18.
 * TODO:
 */

public class LoginFragment extends BaseFragment implements LoginPrestener.ViewListener{


    @BindView(R.id.cet_account)
    CustomEditText mCetAccount;
    @BindView(R.id.cet_password)
    CustomEditText mCetPassword;
    @BindView(R.id.b_login)
    Button mBLogin;
    @BindView(R.id.b_sign_up)
    Button mBSignUp;
    @BindView(R.id.cb_remember)
    AppCompatCheckBox mCbRemember;
    @BindView(R.id.tv_lost_password)
    TextView mTvLostPassword;
    @BindView(R.id.activity_login)
    LinearLayout mActivityLogin;
    Unbinder unbinder;
    private LoginPrestener.PreListener mPreListener;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    protected void initData() {
        mPreListener=  new LoginPresenterCompl(this,mContext);
        mCetAccount.setText("10001");
        mCetPassword.setText("123456a");
    }
    @Override
    protected void initView() {
    }

    @Override
    protected void initRegister() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.b_login, R.id.b_sign_up,R.id.tv_lost_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.b_login:
                ThreadHelper.instance().runOnWorkThread(new Runnable() {
                    @Override
                    public void run() {
                        mPreListener.doLogin(mCetAccount.getText(), AesEncryptionUtil.encrypt(mCetPassword.getText()));
                    }
                });
                showPopupLoading(mActivityLogin);
                break;
            case R.id.b_sign_up:
                EventBus.getDefault().post(new MessageSignUp());
                break;
            case R.id.tv_lost_password:
                EventBus.getDefault().post(new MessageLostPassword());
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimeOut(String timeout) {
        if (timeout.equalsIgnoreCase(SSLSocketChannel.TIMEOUT)) {
            if (popupWindowLoading != null)
                popupWindowLoading.dismiss();
            showToast("服务超时");
//            btnLogin.setEnabled(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginResult(ResponseEvent loginEvent) {
        int code = loginEvent.getResult_code();
        if (code == 0) {
            CacheUtil.saveuserInfo(mContext, mCetAccount.getText().toString(),
                    AesEncryptionUtil.encrypt(mCetPassword.getText().toString()));
            ACache.get(mContext).put(MyConstant.user_name,mCetAccount.getText().toString());
            ACache.get(mContext).put(MyConstant.user_password,AesEncryptionUtil.encrypt(mCetPassword.getText().toString()));
        } else {
            if(code==-100){//服务器出错，超时等
                showToast("网路或者服务器出错了，请稍后重试");
            }else {
                showToast("用户名或者密码错误，请重试");
            }
            if (popupWindowLoading != null) {
                popupWindowLoading.dismiss();
            }
//            mBLogin.setEnabled(true);
        }
    }
    /**
     * 获取所有产品，并且订阅
     *
     * @param allSymbol
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetAllSymbol(BeanSymbolConfig allSymbol) {
        startActivity(new Intent(mContext, TradeActivity.class).putExtra(TradeActivity.ALL_SYMBOLS_DATA,new Gson().toJson(allSymbol,BeanSymbolConfig.class)));
        getActivity().finish();
        Log.i(SystemUtil.getTAG(this), "onGetAllSymbol: ");
    }
}


