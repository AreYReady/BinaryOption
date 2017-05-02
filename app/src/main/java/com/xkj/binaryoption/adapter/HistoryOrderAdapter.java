package com.xkj.binaryoption.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.bean.BeanHistoryOrder;
import com.xkj.binaryoption.utils.BigdecimalUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huangsc on 2017-04-22.
 * TODO:历史持仓数据adapter
 */

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.HistoryHolder> {

    private Context mContext;
    private BeanHistoryOrder mBeanHistoryOrder;

    public HistoryOrderAdapter(Context context, BeanHistoryOrder beanHistoryOrder) {
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
//        BeanOrderResult beanOrderResult = mBeanHistoryOrder.getItems().get(position);
        BeanHistoryOrder.ItemsBean itemsBean=mBeanHistoryOrder.getItems().get(position);
        holder.mTvClosePrice.setText(String.valueOf(itemsBean.getClose_price()));
        holder.mTvBuyPrice.setText(String.valueOf(itemsBean.getOpen_price()));
        holder.mTvAmountAcount.setText(String.valueOf(itemsBean.getMoney()));
        holder.mTvExpireTime.setText(String.valueOf(itemsBean.getClose_time()));
        holder.mTvOrderNumber.setText(String.valueOf(itemsBean.getTicket()));
        holder.mTvSymbol.setText(itemsBean.getSymbol());
//        holder.mTvProfitAmount.setText(String.valueOf(beanOrderResult.getOpen_price()));
        holder.mTvOrderTime.setText(String.valueOf(itemsBean.getOpen_time()));
        switch (itemsBean.getDirection()){
            case 0://跌
                holder.mIvOrderResult.setImageResource(R.mipmap.green);
                break;
            case 1://涨
                holder.mIvOrderResult.setImageResource(R.mipmap.red);
                break;
        }
        switch (itemsBean.getResult()){
            case 1://亏
                holder.mTvProfitAmount.setTextColor(mContext.getResources().getColor(R.color.text_color_price_fall));
                holder.mTvProfitAmountDesc.setTextColor(mContext.getResources().getColor(R.color.text_color_price_fall));
                holder.mTvProfitAmountDesc.setText("亏损金额");
                holder.mTvProfitAmount.setText("-"+BigdecimalUtils.mul(String.valueOf(itemsBean.getMoney()),BigdecimalUtils.movePointLeft(String.valueOf(itemsBean.getCommision_level()),2)));
                break;
            case 2://盈利
                holder.mTvProfitAmount.setTextColor(mContext.getResources().getColor(R.color.text_color_price_rise));
                holder.mTvProfitAmountDesc.setTextColor(mContext.getResources().getColor(R.color.text_color_price_rise));
                holder.mTvProfitAmount.setText(BigdecimalUtils.mul(String.valueOf(itemsBean.getMoney()),BigdecimalUtils.movePointLeft(String.valueOf(itemsBean.getCommision_level()),2)));
                break;
            case 3://平
                holder.mTvProfitAmount.setTextColor(mContext.getResources().getColor(R.color.text_color_price_ping));
                holder.mTvProfitAmountDesc.setTextColor(mContext.getResources().getColor(R.color.text_color_price_ping));
                holder.mTvProfitAmount.setText("0.0");
                break;
        }
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
        @BindView(R.id.tv_profit_amount_desc)
        TextView mTvProfitAmountDesc;
        @BindView(R.id.iv_order_result)
        ImageView mIvOrderResult;
        public HistoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
