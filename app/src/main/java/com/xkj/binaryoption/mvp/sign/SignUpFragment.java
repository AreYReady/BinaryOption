package com.xkj.binaryoption.mvp.sign;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.bean.BeanCurrentServerTime;
import com.xkj.binaryoption.bean.BeanServerTimeForHttp;
import com.xkj.binaryoption.bean.BeanSignUpInfo;
import com.xkj.binaryoption.bean.BeanXCode;
import com.xkj.binaryoption.constant.RequestConstant;
import com.xkj.binaryoption.constant.UrlConstant;
import com.xkj.binaryoption.io.okhttp.OkhttpUtils;
import com.xkj.binaryoption.utils.ACache;
import com.xkj.binaryoption.utils.AesEncryptionUtil;
import com.xkj.binaryoption.utils.DateUtils;
import com.xkj.binaryoption.utils.SystemUtil;
import com.xkj.binaryoption.utils.ThreadHelper;
import com.xkj.binaryoption.widget.CustomEditText;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by huangsc on 2017-04-17.
 * TODO:注册的fragment
 */

public class SignUpFragment extends BaseFragment {


    @BindView(R.id.cet_name)
    CustomEditText mCetName;
    @BindView(R.id.cet_account)
    CustomEditText mCetAccount;
    @BindView(R.id.cet_check)
    CustomEditText mCetCheck;
    @BindView(R.id.cet_password)
    CustomEditText mCetPassword;
    @BindView(R.id.cet_password_again)
    CustomEditText mCetPasswordAgain;
    @BindView(R.id.tv_back_login)
    TextView mTvBackLogin;
    Unbinder unbinder;
    String regPh = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
    String regPassword = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
    @BindView(R.id.b_sign_up)
    AppCompatButton mBSignUp;
    @BindView(R.id.cb_confirm)
    AppCompatCheckBox mCbConfirm;
    @BindView(R.id.tv_confirm_prompt)
    TextView mTvConfirmPrompt;
    private String isXCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initRegister() {

    }

    @Override
    protected void initData() {

        mCetCheck.setOnClickCheckCodeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: ");
              if(!checkPhone()){
                  return;
              }
                if (BeanCurrentServerTime.getInstance().isServerTime()) {
                    sendCodeRequest();
                }else {
                    sendServerTimeHttpRequest(0);
                }
            }
        });
//        mCetAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(!b){
//                    if(!Pattern.compile(regPh).matcher(mCetAccount.getText().toString()).matches()){
//                        Log.i(TAG, "afterTextChanged: 1");
//                        mCetAccount.setPromptText("请填入正确的手机号");
//                        mCetAccount.setPromptVisibility(View.VISIBLE);
//                    }else{
//                        Log.i(TAG, "afterTextChanged: 2");
//                        mCetAccount.setPromptVisibility(View.INVISIBLE);
//                    }
//                }
//            }
//        });
//        mCetName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(!b){
//                    if(!Pattern.compile(regPh).matcher(mCetAccount.getText().toString()).matches()){
//                        Log.i(TAG, "afterTextChanged: 1");
//                        mCetAccount.setPromptText("请填入正确的手机号");
//                        mCetAccount.setPromptVisibility(View.VISIBLE);
//                    }else{
//                        Log.i(TAG, "afterTextChanged: 2");
//                        mCetAccount.setPromptVisibility(View.INVISIBLE);
//                    }
//                }
//            }
//        });
        mCetAccount.setText("15059790550");
        mCetPassword.setText("huang123456");
        mCetPasswordAgain.setText("huang123456");
        mCetName.setText("huang");

    }

    private boolean checkPhone() {
        if (mCetAccount.getText().isEmpty() || !Pattern.compile(regPh).matcher(mCetAccount.getText()).matches()) {
            mCetAccount.setPromptText("请填入正确的手机号码");
            mCetAccount.setPromptVisibility(View.VISIBLE);
            return false;
        } else {
            mCetAccount.setPromptVisibility(View.INVISIBLE);

        }
        return true;
    }

    //发送时间请求 0 请求验证码 1请求注册
    private void sendServerTimeHttpRequest(final int i) {
        OkhttpUtils.enqueue(UrlConstant.URL_SERVICE_TIME, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showfaild("连接失败，请检测网咯");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanServerTimeForHttp beanServerTimeForHttp = new Gson().fromJson(response.body().string(), BeanServerTimeForHttp.class);
                if (beanServerTimeForHttp.getStatus() == 1) {
                    BeanCurrentServerTime.getInstance().setServerTime(DateUtils.getOrderStartTimeNoTimeZone(beanServerTimeForHttp.getData(), "yyyyMMddHHmmss"));
                    if(i==0) {
                        sendCodeRequest();
                    }else{
                        sendSignUpRequest();
                    }
                }
            }
        });
    }

    //发送验证码请求
    private void sendCodeRequest() {
        final Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.PHONE, AesEncryptionUtil.stringBase64toString(mCetAccount.getText()));
        map.put(RequestConstant.xtext, AesEncryptionUtil.stringBase64toString("您的验证码是_XCODE_ 请妥善保存"));
        OkhttpUtils.enqueue(UrlConstant.Send_Xcode, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showfaild("连接失败，请检测网咯");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanXCode beanXCode = new Gson().fromJson(response.body().string(), BeanXCode.class);
                if (beanXCode.getStatus() == 1) {
                    ACache.get(mContext).put(RequestConstant.xtext, map.get(RequestConstant.PHONE).concat(beanXCode.getData().getXcode()), 300);
                    Log.i(TAG, "onResponse: 验证码" + new Gson().toJson(beanXCode));
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.b_sign_up, R.id.tv_back_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.b_sign_up:
                if (!checkData()) {
                    return;
                }
                showDialogLoading();
                if(BeanCurrentServerTime.getInstance().isServerTime()) {
                    sendSignUpRequest();
                }else{
                    sendServerTimeHttpRequest(1);
                }
                break;
            case R.id.tv_back_login:
                Log.i(SystemUtil.getTAG(this), "onViewClicked: ");
                finishFragment();
                break;
        }
    }

    private void finishFragment(){
        getFragmentManager().popBackStack();
    }

    //发送注册信息
    private void sendSignUpRequest() {
        Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.PHONE,AesEncryptionUtil.stringBase64toString(mCetAccount.getText()));
        map.put(RequestConstant.NAME,AesEncryptionUtil.stringBase64toString(mCetName.getText()));
        map.put(RequestConstant.LOGIN_PASSWORD,AesEncryptionUtil.stringBase64toString(mCetPassword.getText()));
        OkhttpUtils.enqueue(UrlConstant.URL_MT4_REG, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showfaild("连接失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanSignUpInfo beanSignUpInfo=new Gson().fromJson(response.body().string(), BeanSignUpInfo.class);
                Log.i(TAG, "onResponse: 注册"+new Gson().toJson(beanSignUpInfo));
                if(beanSignUpInfo.getStatus()==1){
                    closeDialog();
                    showSuccDialog("注册成功", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishFragment();
                        }
                    });
                }else{
                    showfaild(String.format(getString(R.string.action_fail),beanSignUpInfo.getTips()==null?beanSignUpInfo.getMsg():beanSignUpInfo.getTips()));
                }
            }
        });
    }
    private AlertDialog.Builder alertDialog;
    private void showSuccDialog(final String message, final DialogInterface.OnClickListener onClickListener) {
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog = new AlertDialog.Builder(mContext, R.style.AlertDialog_Succ).setTitle("注册成功").setMessage(message).setPositiveButton("确定", onClickListener);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
    }

    //检测数据，看是否可符合要求
    private boolean checkData() {
        boolean isFit = true;
        if (mCetName.getText().isEmpty()) {
            mCetName.setPromptText("用户名不能为空");
            mCetName.setPromptVisibility(View.VISIBLE);
            isFit = false;
        } else {
            mCetName.setPromptVisibility(View.INVISIBLE);
        }
        if (mCetAccount.getText().isEmpty() || !Pattern.compile(regPh).matcher(mCetAccount.getText()).matches()) {
            mCetAccount.setPromptText("请填入正确的手机号码");
            mCetAccount.setPromptVisibility(View.VISIBLE);
            isFit = false;
        } else {
            mCetAccount.setPromptVisibility(View.INVISIBLE);
        }
        if (mCetCheck.getText().isEmpty() || mCetAccount.getText().isEmpty() || mCetAccount.getText().concat(mCetCheck.getText()).equals(ACache.get(mContext).getAsString(RequestConstant.xtext))) {
            mCetCheck.setPromptText("请输入正确的验证码");
            mCetCheck.setPromptVisibility(View.VISIBLE);
            isFit = false;
        } else {
            mCetCheck.setPromptVisibility(View.INVISIBLE);
        }
        if (mCetPassword.getText().isEmpty() || !Pattern.compile(regPassword).matcher(mCetPassword.getText()).matches()) {
            mCetPassword.setPromptText("请输入6到20位数字和字母组合密码");
            mCetPassword.setPromptVisibility(View.VISIBLE);
        } else {
            mCetPassword.setPromptVisibility(View.INVISIBLE);
        }
        if (mCetPasswordAgain.getText().isEmpty() || mCetPassword.getText().isEmpty() || !mCetPasswordAgain.getText().equals(mCetPassword.getText())) {
            mCetPasswordAgain.setPromptText("两次密码不相符，请新输入");
            mCetPasswordAgain.setPromptVisibility(View.VISIBLE);
        } else {
            mCetPasswordAgain.setPromptVisibility(View.INVISIBLE);
        }
        if (mCbConfirm.isChecked()) {
            mTvConfirmPrompt.setVisibility(View.INVISIBLE);
        }else{
            mTvConfirmPrompt.setVisibility(View.VISIBLE);
        }
        return isFit;
    }


}
