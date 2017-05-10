package com.xkj.binaryoption.mvp.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseActivity;
import com.xkj.binaryoption.utils.ToashUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangsc on 2017-05-09.
 * TODO:
 */

public class PayActivity extends BaseActivity {
    @BindView(R.id.b_query)
    Button mBQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initRegister() {
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.b_query)
    public void onViewClicked() {
        ToashUtil.showShort(this,"hh");
    }
}
