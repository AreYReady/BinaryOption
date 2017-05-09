package com.xkj.binaryoption.mvp.sign;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        mCetCheck.setOnClickCheckCodeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"hh",Toast.LENGTH_SHORT).show();
            }
        });
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
        getFragmentManager().popBackStack();
    }
}
