package com.xkj.binaryoption.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huangsc on 2017-04-22.
 * TODO:历史持仓数据adapter
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class HistoryHolder extends  RecyclerView.ViewHolder {
        public HistoryHolder(View itemView) {
            super(itemView);
        }
    }
}
