package com.xkj.binaryoption.mvp.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseActivity;
import com.xkj.binaryoption.base.BaseFragment;
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


}
