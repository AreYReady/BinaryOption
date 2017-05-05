package com.xkj.binaryoption.mvp.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.adapter.ManagerAdapter;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.bean.BeanManages;
import com.xkj.binaryoption.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by huangsc on 2017-05-05.
 * TODO:
 */

public class fragmentManage extends BaseFragment {
    @BindView(R.id.rv_manager)
    RecyclerView mRvManager;
    Unbinder unbinder;
    private List<BeanManages> mBeanManagesList;
    private ManagerAdapter mManagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manage_index, container, false);
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
        mBeanManagesList = new ArrayList<>();
        mBeanManagesList.add(new BeanManages("下单交易",R.mipmap.info01,"持仓记录",R.mipmap.info02));
        mBeanManagesList.add(new BeanManages("预存资金",R.mipmap.info03,"提取资金",R.mipmap.info04));
        mBeanManagesList.add(new BeanManages("绑定微信",R.mipmap.info05));
        mBeanManagesList.add(new BeanManages("分享",R.mipmap.info06));
        mBeanManagesList.add(new BeanManages("个人资料",R.mipmap.info07));
        mBeanManagesList.add(new BeanManages("新手教程",R.mipmap.info08));
        mBeanManagesList.add(new BeanManages("实名认证",R.mipmap.info09));
        mBeanManagesList.add(new BeanManages("退出登录",R.mipmap.info10));
        mRvManager.setAdapter(mManagerAdapter=new ManagerAdapter(mContext, mBeanManagesList));
        mRvManager.setLayoutManager(new LinearLayoutManager(mContext));
        mRvManager.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        mManagerAdapter.setOnItemClickListener(new ManagerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, String desc) {
                Log.i(TAG, "onClick: "+desc);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
