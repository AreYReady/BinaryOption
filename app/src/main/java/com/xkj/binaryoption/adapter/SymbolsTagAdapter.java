package com.xkj.binaryoption.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.bean.BeanSymbolTag;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huangsc on 2017-04-26.
 * TODO:
 */

public class SymbolsTagAdapter extends RecyclerView.Adapter<SymbolsTagAdapter.MyHolder> {
    private List<BeanSymbolTag> mBeanSymbolTags;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private boolean isfrist=true;
    private int oldPosition=0;

    public SymbolsTagAdapter(Context context, List<BeanSymbolTag> beanSymbolTags){
        mContext=context;
        mBeanSymbolTags=beanSymbolTags;
    }
    public interface OnItemClickListener{
        void onClick(int position,String symbols);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener=onItemClickListener;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder myHolder = new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_symbol_tag, parent, false));
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
//                Bundle payload = (Bundle) payloads.get(0);
//            for(payloads.get(0).toString())
//            for(String key:payload.keySet()){{
                switch (payloads.get(0).toString()){
                    case "amount":
                        holder.mTvSymbolAmount.setText(mBeanSymbolTags.get(position).getAmount());
                        if (mBeanSymbolTags.get(position).getUpOrDown()) {
                            holder.mTvSymbolAmount.setTextColor(Color.RED);
                            holder.mIvSymbolIcon.setImageResource(R.mipmap.red);
                        } else {
                            holder.mIvSymbolIcon.setImageResource(R.mipmap.green);
                            holder.mTvSymbolAmount.setTextColor(Color.GREEN);
                        }
                        break;
                    case "background":
                        holder.mVLink.setBackgroundResource(R.color.link_gray);
                        holder.mRlTagParent.setBackground(null);
                        break;
                    case "background_selete":
                        holder.mVLink.setBackgroundResource(R.color.background_button_orange);
                        holder.mRlTagParent.setBackgroundResource(R.color.link_gray);
                        break;


//                }
            }
            }

        }
//    }


    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        if (isfrist) {
            holder.mVLink.setBackgroundResource(R.color.background_button_orange);
            holder.mRlTagParent.setBackgroundResource(R.color.link_gray);
            isfrist=false;
        } else {
            holder.mVLink.setBackgroundResource(R.color.link_gray);
            holder.mRlTagParent.setBackground(null);
        }
//        holder.mRlTagParent.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                setCurrentSymbol(mBeanSymbolTags.get(position).getSymbol());
//                refreshTimeLink(mRealTimeDataMap);
//                notifyItemChanged(mPosition);
//                isChange=true;
//                mPosition = position;
//                notifyItemChanged(position);
//            }
//        });
        holder.mRlTagParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oldPosition==position){
                    return ;
                }
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onClick(position,mBeanSymbolTags.get(position).getSymbol());
                }
                notifyItemChanged(position,"background_selete");
                notifyItemChanged(oldPosition,"background");
                oldPosition=position;
            }
        });
        holder.mTvSymbol.setText(mBeanSymbolTags.get(position).getDesc());
        holder.mTvSymbolAmount.setText(mBeanSymbolTags.get(position).getAmount());
        if (mBeanSymbolTags.get(position).getUpOrDown()) {
            holder.mTvSymbolAmount.setTextColor(Color.RED);
            holder.mIvSymbolIcon.setImageResource(R.mipmap.red);
        } else {
            holder.mIvSymbolIcon.setImageResource(R.mipmap.green);
            holder.mTvSymbolAmount.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return mBeanSymbolTags.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_symbol)
        TextView mTvSymbol;
        @BindView(R.id.rl_tag_parent)
        RelativeLayout mRlTagParent;
        @BindView(R.id.tv_symbol_amount)
        TextView mTvSymbolAmount;
        @BindView(R.id.iv_symbol_icon)
        ImageView mIvSymbolIcon;
        @BindView(R.id.v_link)
        View mVLink;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
}





}