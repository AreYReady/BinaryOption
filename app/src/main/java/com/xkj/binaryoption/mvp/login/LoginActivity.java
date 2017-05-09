package com.xkj.binaryoption.mvp.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseActivity;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.message.MessageDisconnect;
import com.xkj.binaryoption.message.MessageLostPassword;
import com.xkj.binaryoption.message.MessageSignUp;
import com.xkj.binaryoption.mvp.lost_password.LostPassFragment;
import com.xkj.binaryoption.mvp.sign.SignUpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends BaseActivity {
   FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initRegister() {
        EventBus.getDefault().register(this);
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        fragmentManager = getSupportFragmentManager();
        addFragment(R.id.fl_index_context,new LoginFragment());
    }
    private void addFragment(int id, BaseFragment baseFragment){
        fragmentManager.beginTransaction().add(id,baseFragment).addToBackStack(null).commit();
    }
    @Subscribe
    public void getSignUpEvent(MessageSignUp notifaSignUp){
        addFragment(R.id.fl_index_context,new SignUpFragment());
    }
    @Subscribe
    public void getMessageLostPassword(MessageLostPassword messageLostPassword){
        addFragment(R.id.fl_index_context,new LostPassFragment());
    }
//
private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //覆盖方法，不让login重连
    public void getDisConnectMessage(MessageDisconnect messageDisconnect){

    }
    //覆盖方法，
    public void onTimeOut(String timeout){

    }
}
