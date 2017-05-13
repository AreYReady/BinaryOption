package com.xkj.binaryoption.mvp.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.adapter.ManagerAdapter;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.bean.BeanManages;
import com.xkj.binaryoption.bean.BeanUserInfo;
import com.xkj.binaryoption.constant.MyConstant;
import com.xkj.binaryoption.utils.ACache;
import com.xkj.binaryoption.widget.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by huangsc on 2017-05-05.
 * TODO:
 */

public class FragmentManage extends BaseFragment {
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.rv_manager)
    RecyclerView mRvManager;
    Unbinder unbinder;
    @BindView(R.id.tv_login_group)
    TextView mTvLoginGroup;
    private List<BeanManages> mBeanManagesList;
    private ManagerAdapter mManagerAdapter;
    private ManagerAdapter.OnItemClickListener mOnItemClickListener;
    private String[] mItemString;
    private BeanUserInfo mBeanUserInfo;

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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        mItemString = mContext.getResources().getStringArray(R.array.item_names);
        mBeanManagesList = new ArrayList<>();
        mBeanManagesList.add(new BeanManages(mItemString[0], R.mipmap.info01, mItemString[1], R.mipmap.info02));
        mBeanManagesList.add(new BeanManages(mItemString[2], R.mipmap.info03, mItemString[3], R.mipmap.info04));
        mBeanManagesList.add(new BeanManages(mItemString[4], R.mipmap.info05));
        mBeanManagesList.add(new BeanManages(mItemString[5], R.mipmap.info06));
        mBeanManagesList.add(new BeanManages(mItemString[6], R.mipmap.info07));
        mBeanManagesList.add(new BeanManages(mItemString[7], R.mipmap.info08));
        mBeanManagesList.add(new BeanManages(mItemString[8], R.mipmap.info09));
        mBeanManagesList.add(new BeanManages(mItemString[9], R.mipmap.info10));
        mRvManager.setAdapter(mManagerAdapter = new ManagerAdapter(mContext, mBeanManagesList));
        mRvManager.setLayoutManager(new LinearLayoutManager(mContext));
        mRvManager.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        mManagerAdapter.setOnItemClickListener(new ManagerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, String desc) {
                Log.i(TAG, "onClick: " + desc);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position, desc);
                }
            }
        });
        mTvLogin.setText(ACache.get(mContext).getAsString(MyConstant.user_name));
            mTvLoginGroup.setText(mBeanUserInfo.getName());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventUserInfo(BeanUserInfo beanUserInfo) {
        mBeanUserInfo = beanUserInfo;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setOnItemClickListener(ManagerAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
