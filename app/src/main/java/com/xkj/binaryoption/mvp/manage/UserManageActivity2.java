package com.xkj.binaryoption.mvp.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.adapter.ManagerAdapter;
import com.xkj.binaryoption.base.BaseActivity;
import com.xkj.binaryoption.bean.BeanManages;

import java.util.List;

/**
 * Created by huangsc on 2017-05-04.
 * TODO:
 */

public class UserManageActivity2 extends BaseActivity {

//    @BindView(R.id.tv_login)
//    TextView mTvLogin;
//    @BindView(R.id.rv_manager)
//    RecyclerView mRvManager;
//    @BindView(R.id.ll_index)
//    LinearLayout mLlIndex;
//    @BindView(R.id.ll_user_manager)
//    LinearLayout mLlUserManager;
    Button mButton;
    private ManagerAdapter mManagerAdapter;

    private List<BeanManages> mBeanManagesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_manage);
//        ButterKnife.bind(this);
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
//        mBeanManagesList = new ArrayList<>();
//        mBeanManagesList.add(new BeanManages("下单交易",R.mipmap.info01,"持仓记录",R.mipmap.info02));
//        mBeanManagesList.add(new BeanManages("预存资金",R.mipmap.info03,"提取资金",R.mipmap.info04));
//        mBeanManagesList.add(new BeanManages("绑定微信",R.mipmap.info05));
//        mBeanManagesList.add(new BeanManages("分享",R.mipmap.info06));
//        mBeanManagesList.add(new BeanManages("个人资料",R.mipmap.info07));
//        mBeanManagesList.add(new BeanManages("新手教程",R.mipmap.info08));
//        mBeanManagesList.add(new BeanManages("实名认证",R.mipmap.info09));
//        mBeanManagesList.add(new BeanManages("退出登录",R.mipmap.info10));
//        mRvManager.setAdapter(mManagerAdapter=new ManagerAdapter(mContext, mBeanManagesList));
//        mRvManager.setLayoutManager(new LinearLayoutManager(mContext));
//        mRvManager.addItemDecoration(new DividerItemDecoration(mContext,
//                DividerItemDecoration.VERTICAL_LIST));
//        mManagerAdapter.setOnItemClickListener(new ManagerAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int position, String desc) {
//                Log.i(TAG, "onClick: "+desc);
//
//            }
//        });
    }

}
