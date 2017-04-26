package com.xkj.binaryoption.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.bean.BeanHistoryOrder;
import com.xkj.binaryoption.bean.BeanOrderResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huangsc on 2017-04-22.
 * TODO:历史持仓数据adapter
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    private Context mContext;
    private BeanHistoryOrder mBeanHistoryOrder;

    public HistoryAdapter(Context context, BeanHistoryOrder beanHistoryOrder) {
        this.mBeanHistoryOrder = beanHistoryOrder;
        mContext = context;
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HistoryHolder viewHolder = new HistoryHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_history_pending, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        BeanOrderResult beanOrderResult = mBeanHistoryOrder.getItems().get(position);
        holder.mTvClosePrice.setText(String.valueOf(beanOrderResult.getClose_price()));
        holder.mTvBuyPrice.setText(String.valueOf(beanOrderResult.getOpen_price()));
        holder.mTvAmountAcount.setText(String.valueOf(beanOrderResult.getMoney()));
        holder.mTvExpireTime.setText(String.valueOf(beanOrderResult.getClose_time()));
        holder.mTvOrderNumber.setText(String.valueOf(beanOrderResult.getTicket()));
        holder.mTvSymbol.setText(beanOrderResult.getSymbol());
//        holder.mTvProfitAmount.setText(String.valueOf(beanOrderResult.getOpen_price()));
        holder.mTvOrderTime.setText(String.valueOf(beanOrderResult.getOpen_time()));
    }

    @Override
    public int getItemCount() {
        return mBeanHistoryOrder.getCount();
//        return  20;
    }

    class HistoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_symbol)
        TextView mTvSymbol;
        @BindView(R.id.tv_buy_price)
        TextView mTvBuyPrice;
        @BindView(R.id.tv_close_price)
        TextView mTvClosePrice;
        @BindView(R.id.tv_amount_acount)
        TextView mTvAmountAcount;
        @BindView(R.id.tv_order_number)
        TextView mTvOrderNumber;
        @BindView(R.id.tv_order_time)
        TextView mTvOrderTime;
        @BindView(R.id.tv_expire_time)
        TextView mTvExpireTime;
        @BindView(R.id.tv_profit_amount)
        TextView mTvProfitAmount;
        public HistoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
