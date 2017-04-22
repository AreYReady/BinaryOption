package com.xkj.binaryoption.mvp.trade.pending.now;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseFragment;

/**
 * Created by huangsc on 2017-04-22.
 * TODO:当前持仓数据
 */

public class CurrentFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_demo,container,false);
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
}
