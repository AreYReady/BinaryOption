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
import com.xkj.binaryoption.bean.BeanUserInfo;
import com.xkj.binaryoption.message.MessageManageItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private String[] mItemStrings;
    private BeanUserInfo mBeanUserInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_manage);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initRegister() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        FragmentManage fragmentManage;
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_index_context,fragmentManage= new FragmentManage()).commit();
        fragmentManage.setOnItemClickListener(new ManagerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, String desc) {
                checkDescAction(desc);
            }
        });
    }

    /**
     * 处理点击事件响应
     * @param desc
     */
    private void checkDescAction(String desc) {
        switch (desc){
            case "下单交易":
            case "持仓记录":
                EventBus.getDefault().post(new MessageManageItem(desc));
                finish();
                break;
            case "预存资金":
                break;
            case "提取资金":
                break;
            case "绑定微信":
                break;
            case "分享":
                break;
            case "个人资料":
                break;
            case "新手教程":
                break;
            case "实名认证":
                break;
            case "退出登录":
                break;
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventUserInfo(BeanUserInfo beanUserInfo){
        mBeanUserInfo=beanUserInfo;
        if(mTvLogin!=null){
            mTvLogin.setText(beanUserInfo.getLogin());
        }
    }
    @Override
    protected void initData() {

        mItemStrings=getResources().getStringArray(R.array.item_names);
        if(mBeanUserInfo!=null){
            mTvLogin.setText(String.valueOf(mBeanUserInfo.getLogin()));
        }
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
