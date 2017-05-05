package com.xkj.binaryoption.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.bean.BeanSymbolConfig;

/**
 * Created by huangsc on 2017-05-05.
 * TODO:
 */

public class TimePeriodAdapter extends RecyclerView.Adapter<TimePeriodAdapter.MyHolder> {
    private Context mContext;
    private BeanSymbolConfig.SymbolsBean  mTimePeriod;
    public TimePeriodAdapter(Context context, BeanSymbolConfig.SymbolsBean mTimePeriod){
        this.mContext=context;
        this.mTimePeriod=mTimePeriod;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_time_period_,parent,false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mTimePeriod.getCycles()==null?0:mTimePeriod.getCycles().size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
