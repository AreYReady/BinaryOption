package com.xkj.binaryoption.mvp.trade.pending.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.adapter.HistoryOrderAdapter;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.bean.BeanHistoryOrder;
import com.xkj.binaryoption.bean.BeanOrderResult;
import com.xkj.binaryoption.utils.ThreadHelper;
import com.xkj.binaryoption.widget.DividerItemDecoration;
import com.xkj.binaryoption.widget.WrapContentLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

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
    private BeanHistoryOrder mBeanHistoryOrder;
    private HistoryOrderAdapter mHistoryOrderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
            initRv();

    }

    private void initRv() {
        mRvHistoryInfo.setAdapter(mHistoryOrderAdapter=new HistoryOrderAdapter(mContext,mBeanHistoryOrder));
        mRvHistoryInfo.setLayoutManager(new WrapContentLinearLayoutManager(mContext));
        mRvHistoryInfo.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    protected void initRegister() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void EventHistoryOrder(BeanHistoryOrder beanHistoryOrder){
        //把倒序的数据排正
        mBeanHistoryOrder=beanHistoryOrder;
        if(mHistoryOrderAdapter!=null){
            mHistoryOrderAdapter.setData(mBeanHistoryOrder);
            mHistoryOrderAdapter.notifyDataSetChanged();
        }
//        mBeanHistoryOrder.setCount(beanHistoryOrder.getCount());
//        mBeanHistoryOrder.setMsg_type(beanHistoryOrder.getMsg_type());
//        mBeanHistoryOrder.setItems(new ArrayList<BeanHistoryOrder.ItemsBean>());
//        for(int i=beanHistoryOrder.getCount()-1;i>=0;i--){
//            mBeanHistoryOrder.getItems().add(beanHistoryOrder.getItems().get(i));
//        }
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void eventOrderResult (BeanOrderResult beanOrderResult){
        if(beanOrderResult.getStatus()==1){
            final BeanHistoryOrder.ItemsBean itemsBean=new BeanHistoryOrder.ItemsBean();
            itemsBean.setResult(beanOrderResult.getResult());
            itemsBean.setClose_price(String.valueOf(beanOrderResult.getClose_price()));
            itemsBean.setTime_span(beanOrderResult.getTime_span());
            itemsBean.setTicket(beanOrderResult.getTicket());
            itemsBean.setSymbol(beanOrderResult.getSymbol());
            itemsBean.setClose_time(beanOrderResult.getClose_time());
            itemsBean.setCommision_level(beanOrderResult.getCommision_level());
            itemsBean.setDirection(beanOrderResult.getDirection());
            itemsBean.setMoney((int)beanOrderResult.getMoney());
            itemsBean.setOpen_price(String.valueOf(beanOrderResult.getOpen_price()));
            itemsBean.setOpen_time(beanOrderResult.getOpen_time());
            if(mBeanHistoryOrder.getItems()==null){
                mBeanHistoryOrder.setItems(new ArrayList<BeanHistoryOrder.ItemsBean>());
            }

            ThreadHelper.instance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBeanHistoryOrder.getItems().add(mBeanHistoryOrder.getCount(),itemsBean);
                    if(mHistoryOrderAdapter!=null){
                        mHistoryOrderAdapter.setData(mBeanHistoryOrder);
                        mHistoryOrderAdapter.notifyItemInserted(0);
                        mHistoryOrderAdapter.notifyItemRangeChanged(0,mBeanHistoryOrder.getCount());
//                        mRvHistoryInfo.smoothScrollToPosition(0);
                    }
                }
            });
        }

    }
}
