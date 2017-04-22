package com.xkj.binaryoption.mvp.lost_password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.utils.SystemUtil;
import com.xkj.binaryoption.widget.CustomEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by huangsc on 2017-04-17.
 * TODO:注册的fragment
 */

public class LostPassFragment extends BaseFragment {


    @BindView(R.id.cet_account)
    CustomEditText mCetAccount;
    @BindView(R.id.cet_password)
    CustomEditText mCetPassword;
    @BindView(R.id.cet_password_again)
    CustomEditText mCetPasswordAgain;
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

    @OnClick(R.id.tv_back_login)
    public void onViewClicked() {
        Log.i(SystemUtil.getTAG(this), "onViewClicked: ");
        getFragmentManager().popBackStackImmediate();
    }
}
