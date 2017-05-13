package com.xkj.binaryoption.mvp.login.lostpasswd;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.bean.BeanCrUserList;
import com.xkj.binaryoption.bean.BeanCurrentServerTime;
import com.xkj.binaryoption.bean.BeanPassword;
import com.xkj.binaryoption.bean.BeanServerTimeForHttp;
import com.xkj.binaryoption.bean.BeanXCode;
import com.xkj.binaryoption.constant.RequestConstant;
import com.xkj.binaryoption.constant.UrlConstant;
import com.xkj.binaryoption.io.okhttp.OkhttpUtils;
import com.xkj.binaryoption.utils.ACache;
import com.xkj.binaryoption.utils.AesEncryptionUtil;
import com.xkj.binaryoption.utils.DateUtils;
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

import static com.xkj.binaryoption.constant.MyConstant.regPassword;
import static com.xkj.binaryoption.constant.MyConstant.regPh;

/**
 * Created by huangsc on 2017-05-12.
 * TODO:
 */

public class LostPasswdFragment extends BaseFragment {
    @BindView(R.id.cet_account)
    CustomEditText mCetAccount;
    @BindView(R.id.cet_check)
    CustomEditText mCetCheck;
    @BindView(R.id.cet_password)
    CustomEditText mCetPassword;
    @BindView(R.id.cet_password_again)
    CustomEditText mCetPasswordAgain;
    @BindView(R.id.b_lost_password)
    Button mBLostPassword;
    @BindView(R.id.tv_back_login)
    TextView mTvBackLogin;
    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lost_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        mCetCheck.setOnClickCheckCodeListener(new CustomEditText.OnClickCheckCodeListener() {
            @Override
            public boolean onClick(View view) {
                Log.i(TAG, "onClick: ");
                if (!checkPhone()) {
                    return false;
                }
                if (BeanCurrentServerTime.getInstance().isServerTime()) {
                    sendCodeRequest();
                } else {
                    sendServerTimeHttpRequest(0);
                }
                return true;
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

    //发送时间请求 0 请求验证码 1请求重置密码
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
                    if (i == 0) {
                        sendCodeRequest();
                    } else {
                       doLostPasswd();
                    }
                }
            }
        });
    }
    //发送查询账号mt4login
   private void  sendQueryMt4Request(){
       Map<String ,String> map=new TreeMap<>();
       map.put(RequestConstant.PHONE, AesEncryptionUtil.stringBase64toString(mCetAccount.getText().toString()));
       OkhttpUtils.enqueue(UrlConstant.URL_MT4_CRMUSERLIST, map, new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               showfaild("连接失败，请检测网络");
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               BeanCrUserList beanCrUserList=new Gson().fromJson(response.body().string(),BeanCrUserList.class);
               Log.i(TAG, "onResponse: 查询mt4账号列表"+new Gson().toJson(beanCrUserList));
               if(beanCrUserList.getStatus()==1){
                   if(beanCrUserList.getData()==null||beanCrUserList.getData().get(0).getMt4lists()==null){
                       showfaild("该手机号未注册账号");
                   }else{
                       sendLostPasswordRequest(beanCrUserList.getData().get(0).getMt4lists().get(0).getLogin());
                   }
               }else{
                   showfaild(beanCrUserList.getMsg());
               }
           }
       });
   }

    //发送改密的消息
    private void sendLostPasswordRequest(String account) {
        Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(account));
        map.put(RequestConstant.NEW_PSWORD, AesEncryptionUtil.stringBase64toString(mCetPassword.getText()));
        map.put(RequestConstant.ACTION, RequestConstant.Action.CHANGE_MT_PWD.toString());
        OkhttpUtils.enqueue(UrlConstant.URL_MT4_CHANGESET, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showfaild("连接失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanPassword beanPassword = new Gson().fromJson(response.body().string(), BeanPassword.class);
                Log.i(TAG, "onResponse: 密码" + new Gson().toJson(beanPassword));
                if (beanPassword.getStatus() == 1) {
                    closeDialog();
                    showSuccDialog("密码修改成功", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishFragment();
                        }
                    });
                } else {
                    showfaild(String.format(getString(R.string.action_fail), beanPassword.getMsg()));
                }
            }
        });
    }

    private AlertDialog.Builder alertDialog;

    private void showSuccDialog(final String message, final DialogInterface.OnClickListener onClickListener) {
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog = new AlertDialog.Builder(mContext, R.style.AlertDialog_Succ).setTitle("重置密码成功").setMessage(message).setPositiveButton("确定", onClickListener);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
    }

    private void finishFragment() {
        getFragmentManager().popBackStack();
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


    //检测数据，看是否可符合要求
    private boolean checkData() {
        boolean isFit = true;
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
        return isFit;
    }


    @Override
    protected void initRegister() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.b_lost_password, R.id.tv_back_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.b_lost_password:
              doLostPasswd();
                break;
            case R.id.tv_back_login:
                finishFragment();
                break;
        }
    }
    private void doLostPasswd(){
        if (!checkData()) {
            return;
        }
        showDialogLoading();
        if(BeanCurrentServerTime.getInstance().isServerTime()) {
            sendQueryMt4Request();
        }else{
            sendServerTimeHttpRequest(1);
        }
    }
}
