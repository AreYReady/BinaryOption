package com.xkj.binaryoption.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.bean.BeanCurrentOrder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huangsc on 2017-04-22.
 * TODO:当前持仓数据adapter
 */

public class CurrentOrderAdapter extends RecyclerView.Adapter<CurrentOrderAdapter.CurrentHolder> {


    private Context mContext;
    private BeanCurrentOrder mBeanCurrentOrder;

    public CurrentOrderAdapter(Context context, BeanCurrentOrder beanCurrentOrder) {
        this.mBeanCurrentOrder = beanCurrentOrder;
        mContext = context;
    }

    @Override
    public CurrentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CurrentHolder viewHolder = new CurrentHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_current_order_pending, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CurrentHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mBeanCurrentOrder.getOrders()==null?0:mBeanCurrentOrder.getOrders().size();
//        return  20;
    }

    class CurrentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_symbol)
        TextView mTvSymbol;
        @BindView(R.id.tv_buy_price)
        TextView mTvBuyPrice;
        @BindView(R.id.tv_amount_acount)
        TextView mTvAmountAcount;
        @BindView(R.id.tv_current_price)
        TextView mTvCurrentPrice;
        @BindView(R.id.tv_order_time)
        TextView mTvOrderTime;
        public CurrentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
