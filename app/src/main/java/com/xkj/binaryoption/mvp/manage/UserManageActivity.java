package com.xkj.binaryoption.mvp.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.adapter.ManagerAdapter;
import com.xkj.binaryoption.base.BaseActivity;
import com.xkj.binaryoption.bean.BeanManages;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangsc on 2017-05-04.
 * TODO:
 */

public class UserManageActivity extends BaseActivity {


    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.fl_index_context)
    FrameLayout mFlIndexContext;
    @BindView(R.id.ll_index)
    LinearLayout mLlIndex;
    @BindView(R.id.ll_user_manager)
    LinearLayout mLlUserManager;
    private ManagerAdapter mManagerAdapter;
    private List<BeanManages> mBeanManagesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_manage);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initRegister() {

    }

    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_index_context, new fragmentManage()).commit();
    }

    @Override
    protected void initData() {
    }

    @OnClick({R.id.ll_index, R.id.ll_user_manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_index:
                finish();
                break;
            case R.id.ll_user_manager:
                break;
        }
    }
}
