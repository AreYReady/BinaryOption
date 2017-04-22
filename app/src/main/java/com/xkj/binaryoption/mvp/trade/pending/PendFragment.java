package com.xkj.binaryoption.mvp.trade.pending;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.mvp.trade.pending.history.HistoryFragment;
import com.xkj.binaryoption.mvp.trade.pending.now.CurrentFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by huangsc on 2017-04-18.
 * TODO:
 */

public class PendFragment extends BaseFragment {
    @BindView(R.id.tb_now_or_history)
    TabLayout mTbNowOrHistory;
    @BindView(R.id.vp_trade_info)
    ViewPager mVpTradeInfo;
    Unbinder unbinder;
    private List<String> mStrings;
    private List<BaseFragment> mFragmentList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pend, container, false);
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
        if(mStrings==null){
            mStrings=new ArrayList<>();
        }
        if(mFragmentList==null){
            mFragmentList=new ArrayList<>();
        }
        mStrings.add("当前持仓");
        mStrings.add("历史持仓");
        mFragmentList.add(new CurrentFragment());
        mFragmentList.add(new HistoryFragment());
        mVpTradeInfo.setAdapter(new MyPagerAdapter(getFragmentManager()));
        mTbNowOrHistory.setupWithViewPager(mVpTradeInfo);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    class  MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList==null?0:mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mStrings.get(position);
        }
    }
}
