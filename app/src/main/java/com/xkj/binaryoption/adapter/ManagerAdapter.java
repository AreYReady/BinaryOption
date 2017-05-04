package com.xkj.binaryoption.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.bean.BeanManages;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huangsc on 2017-05-04.
 * TODO:
 */

public class ManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<BeanManages> mBeanManages;

    public ManagerAdapter(Context context, List<BeanManages> beanManages) {
        this.mContext = context;
        this.mBeanManages = beanManages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new MyHolder2(LayoutInflater.from(mContext).inflate(R.layout.rv_manage_holder_2, parent, false));
        else {
            return new MyHolder1(LayoutInflater.from(mContext).inflate(R.layout.rv_manage_holder_1, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder1){
            MyHolder1 holder1 = (MyHolder1) holder;
            holder1.mLlInfo01.setTag(mBeanManages.get(position).getDesc1());
            holder1.mTvDesc.setText(mBeanManages.get(position).getDesc1());
            holder1.mLlInfo01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClickListener!=null){
                        mOnItemClickListener.onClick(position,view.getTag().toString());
                    }
                }
            });
            holder1.mIvImage.setImageResource(mBeanManages.get(position).getImageRe1());
        }
        if(holder instanceof MyHolder2){
            MyHolder2 holder2 = (MyHolder2) holder;
            holder2.mIvImage1.setImageResource(mBeanManages.get(position).getImageRe1());
            holder2.mIvImage2.setImageResource(mBeanManages.get(position).getImageRe2());
            holder2.mTvDesc1.setText(mBeanManages.get(position).getDesc1());
            holder2.mTvDesc2.setText(mBeanManages.get(position).getDesc2());
            holder2.mLlItem1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClickListener!=null){
                        mOnItemClickListener.onClick(position,mBeanManages.get(position).getDesc1());
                    }
                }
            });
            holder2.mLlItem2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClickListener!=null){
                        mOnItemClickListener.onClick(position,mBeanManages.get(position).getDesc2());
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return 8;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 2) {
            return 0;
        } else {
            return 1;
        }
    }

    class MyHolder1 extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView mIvImage;
        @BindView(R.id.tv_desc)
        TextView mTvDesc;
        @BindView(R.id.ll_info01)
        LinearLayout mLlInfo01;
        public MyHolder1(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class MyHolder2 extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image1)
        ImageView mIvImage1;
        @BindView(R.id.tv_desc1)
        TextView mTvDesc1;
        @BindView(R.id.iv_image2)
        ImageView mIvImage2;
        @BindView(R.id.tv_desc2)
        TextView mTvDesc2;
        @BindView(R.id.ll_item1)
        LinearLayout mLlItem1;
        @BindView(R.id.ll_item2)
        LinearLayout mLlItem2;

        public MyHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnItemClickListener{
        void onClick(int position,String desc);
    }
    public OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener=onItemClickListener;
    }

}
