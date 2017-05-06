package com.xkj.binaryoption.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.bean.BeanSymbolConfig;
import com.xkj.binaryoption.constant.MyConstant;

/**
 * Created by huangsc on 2017-05-05.
 * TODO:
 */

public class TimePeriodAdapter extends RecyclerView.Adapter<TimePeriodAdapter.MyHolder> {
    private Context mContext;
    private BeanSymbolConfig.
            SymbolsBean  mTimePeriod;
    private OnClickListener mOnClickListener;
    private MyConstant.BuyAciton mBuyAciton;
    private int status=0;
    public TimePeriodAdapter(Context context, BeanSymbolConfig.SymbolsBean mTimePeriod,MyConstant.BuyAciton buyAciton){
        this.mContext=context;
        this.mTimePeriod=mTimePeriod;
        this.mBuyAciton=buyAciton;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder myHolder =new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_time_period_,parent,false));
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        if (position == status) {
            holder.mTextView.setSelected(true);
        }else{
            holder.mTextView.setSelected(false);
        }
        holder.mTextView.setText(String.valueOf(mTimePeriod.getCycles().get(position).getCycle()));
        if(mBuyAciton== MyConstant.BuyAciton.BUY_DOWN){
            holder.mTextView.setBackgroundResource(R.drawable.button_select_green);
        }else{
            holder.mTextView.setBackgroundResource(R.drawable.button_select_red);
        }
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = position;
                if(mOnClickListener!=null){
                    mOnClickListener.onClick(status);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTimePeriod.getCycles()==null?0:mTimePeriod.getCycles().size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private LinearLayout mLinearLayout;

        public MyHolder(View itemView) {
            super(itemView);
            mTextView=(TextView)itemView.findViewById(R.id.tv_expire_time);
            mLinearLayout=(LinearLayout) itemView.findViewById(R.id.ll_item_expire_time);
        }
    }
    public  interface  OnClickListener{
        void onClick(int position);
    }
    public void setOnClickListener(OnClickListener listener){
        mOnClickListener=listener;
    }

}
