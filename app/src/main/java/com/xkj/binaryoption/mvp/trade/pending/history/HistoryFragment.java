package com.xkj.binaryoption.mvp.trade.pending.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by huangsc on 2017-04-22.
 * TODO:历史持仓数据
 */

public class HistoryFragment extends BaseFragment {
    @BindView(R.id.rv_history_info)
    RecyclerView mRvHistoryInfo;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_demo, container, false);
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
}
