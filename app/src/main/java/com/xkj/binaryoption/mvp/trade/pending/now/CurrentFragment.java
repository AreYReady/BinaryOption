package com.xkj.binaryoption.mvp.trade.pending.now;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.adapter.CurrentOrderAdapter;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.bean.BeanCurrentOrder;
import com.xkj.binaryoption.bean.BeanOrderResult;
import com.xkj.binaryoption.bean.RealTimeDataList;
import com.xkj.binaryoption.utils.ThreadHelper;
import com.xkj.binaryoption.widget.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by huangsc on 2017-04-22.
 * TODO:当前持仓数据
 */

public class CurrentFragment extends BaseFragment {
    @BindView(R.id.rv_current_order_info)
    RecyclerView mRvCurrentOrderInfo;
    Unbinder unbinder;
    private BeanCurrentOrder mBeanCurrentOrder;
    private CurrentOrderAdapter mCurrentOrderAdapter;
    CountDownTimer cdt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        mRvCurrentOrderInfo.setAdapter(mCurrentOrderAdapter=new CurrentOrderAdapter(mContext,mBeanCurrentOrder));
        mRvCurrentOrderInfo.setLayoutManager(new LinearLayoutManager(mContext));
        mRvCurrentOrderInfo.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        countdown();

    }

    private void countdown() {
        cdt = new CountDownTimer(Integer.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(mBeanCurrentOrder.getOrders()==null||mBeanCurrentOrder.getOrders().size()==0){
                    return;
                }
                ThreadHelper.instance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0;i<mBeanCurrentOrder.getOrders().size();i++){
                            mBeanCurrentOrder.getOrders().get(i).setLeft_time(mBeanCurrentOrder.getOrders().get(i).getLeft_time()-1000);
                        }
                        mCurrentOrderAdapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public void onFinish() {

            }
        };
        cdt.start();
    }

    @Override
    protected void initRegister() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventCurrentOrder(BeanCurrentOrder beanCurrentOrder) {
        mBeanCurrentOrder = beanCurrentOrder;
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void eventOrderResult(BeanOrderResult beanOrderResult){
        if(beanOrderResult.getStatus()==0){
            //订单开始
            if(mBeanCurrentOrder.getOrders()==null){
                mBeanCurrentOrder.setOrders(new ArrayList<BeanCurrentOrder.OrdersBean>());
            }
            BeanCurrentOrder.OrdersBean ordersBean=new BeanCurrentOrder.OrdersBean();
            ordersBean.setLeft_time(beanOrderResult.getTime_span()*1000);
            ordersBean.setClose_price(String.valueOf(beanOrderResult.getClose_price()));
            ordersBean.setCommision_level(beanOrderResult.getCommision_level());
            ordersBean.setDirection(beanOrderResult.getDirection());
            ordersBean.setMoney((int)beanOrderResult.getMoney());
            ordersBean.setOpen_price(String.valueOf(beanOrderResult.getOpen_price()));
            ordersBean.setOpen_time(beanOrderResult.getOpen_time());
            ordersBean.setSymbol(beanOrderResult.getSymbol());
            ordersBean.setTicket(beanOrderResult.getTicket());
            ordersBean.setTime_span(beanOrderResult.getTime_span());
            mBeanCurrentOrder.getOrders().add(0,ordersBean);
        }else{
            //订单结束
//            for(mBeanCurrentOrder.)
            for(int i=0;i<mBeanCurrentOrder.getOrders().size();i++){
                BeanCurrentOrder.OrdersBean ordersBean = mBeanCurrentOrder.getOrders().get(i);
                if(ordersBean.getTicket()==beanOrderResult.getTicket()){
                    final int finalI = i;
                    ThreadHelper.instance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCurrentOrderAdapter.notifyItemRemoved(finalI);
                            mBeanCurrentOrder.getOrders().remove(finalI);
                        }
                    });
                    break;
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void eventRealTimePrice(RealTimeDataList realTimeDataList){
        if(mBeanCurrentOrder!=null||mBeanCurrentOrder.getOrders()!=null||mBeanCurrentOrder.getOrders().size()!=0) {
            for(int i=0;i<mBeanCurrentOrder.getOrders().size();i++){
                BeanCurrentOrder.OrdersBean ordersBean = mBeanCurrentOrder.getOrders().get(i);
                for (RealTimeDataList.BeanRealTime realTimeData : realTimeDataList.getQuotes()) {
                    if(ordersBean.getSymbol().equals(realTimeData.getSymbol())){
                        Log.i(TAG, "eventRealTimePrice: "+realTimeData.getBid());
                        ordersBean.setClose_price(String.valueOf(realTimeData.getBid()));
                    }
                }
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cdt.onFinish();
        unbinder.unbind();
    }
}
