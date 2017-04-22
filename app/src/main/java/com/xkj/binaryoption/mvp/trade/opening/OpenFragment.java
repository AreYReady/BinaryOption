package com.xkj.binaryoption.mvp.trade.opening;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.bean.BeanSymbolConfig;
import com.xkj.binaryoption.bean.BeanSymbolTag;
import com.xkj.binaryoption.bean.RealTimeDataList;
import com.xkj.binaryoption.mvp.trade.TradeActivity;
import com.xkj.binaryoption.mvp.trade.opening.contract.OpenContract;
import com.xkj.binaryoption.mvp.trade.opening.presenter.OpenPresenterImpl;
import com.xkj.binaryoption.utils.ThreadHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by huangsc on 2017-04-18.
 * TODO:
 */

public class OpenFragment extends BaseFragment implements OpenContract.View{

    @BindView(R.id.tl_type)
    TabLayout mTlType;
    @BindView(R.id.tv_up_amount)
    TextView mTvUpAmount;
    @BindView(R.id.b_up)
    FrameLayout mBUp;
    @BindView(R.id.tv_down_amount)
    TextView mTvDownAmount;
    @BindView(R.id.b_down)
    FrameLayout mBDown;
    Unbinder unbinder;
    @BindView(R.id.rv_symbols)
    RecyclerView mRvSymbols;
    private List<BeanSymbolTag> mBeanSymbolTags;
    private List<BeanSymbolTag> mDupBeanSymbolTags;
    private String mAllSubSymbols;
    BeanSymbolConfig beanSymbolConfig;
    private OpenContract.Presenter mPresenter;
    private final String AMOUNT_CHANG="amountChang";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_opening, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
MyRecycleAdapter mMyRecycleAdapter;
    @Override
    protected void initView() {
        mRvSymbols.setAdapter(mMyRecycleAdapter=new MyRecycleAdapter());
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvSymbols.setLayoutManager(layoutManager);
        mTlType.addTab(mTlType.newTab().setText("分时图"));
        mTlType.addTab(mTlType.newTab().setText("5分钟"));
        mTlType.addTab(mTlType.newTab().setText("15分钟"));
        mTlType.addTab(mTlType.newTab().setText("60分钟"));
        mTlType.addTab(mTlType.newTab().setText("日线"));
        LinearLayout linearLayout = (LinearLayout) mTlType.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(mContext,
                R.drawable.layout_divider_vertical));
    }

    @Override
    protected void initRegister() {
        mPresenter=new OpenPresenterImpl(mContext,this);
    }

    @Override
    protected void initData() {
        mBeanSymbolTags=new ArrayList<>();
        mDupBeanSymbolTags=new ArrayList<>();
        mAllSubSymbols=getArguments().getString(TradeActivity.ALL_SYMBOLS_DATA);
        beanSymbolConfig = new Gson().fromJson(mAllSubSymbols, BeanSymbolConfig.class);
        for(BeanSymbolConfig.SymbolsBean symbolsBean:beanSymbolConfig.getSymbols()){
            mBeanSymbolTags.add(new BeanSymbolTag(symbolsBean.getDesc(),symbolsBean.getSymbol(),"0.0",true));
            mDupBeanSymbolTags.add(new BeanSymbolTag(symbolsBean.getDesc(),symbolsBean.getSymbol(),"0.0",true));
            mPresenter.sendSubSymbol(symbolsBean.getSymbol());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    int i = 1;

    @OnClick({R.id.b_up, R.id.b_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.b_up:
                mTlType.getTabAt(1).setText("" + i++);
                break;
            case R.id.b_down:
                break;
        }
    }
    private int mPosition =0;

    /**
     * 获取实时数据
     * @param realTimeDataList
     */
    DiffUtil.DiffResult diffResult;
    @Override
    public void receRealTimeData(RealTimeDataList realTimeDataList) {
        Log.i(TAG, "receRealTimeData: ");
        BeanSymbolTag beanSymbolTag;
        for(RealTimeDataList.BeanRealTime beanRealTime:realTimeDataList.getQuotes()){

            for(int i=0;i<mBeanSymbolTags.size();i++){
                beanSymbolTag=mBeanSymbolTags.get(i);
                if(beanSymbolTag.getSymbol().equals(beanRealTime.getSymbol())){
                    if(beanRealTime.getAsk()>Double.valueOf(beanSymbolTag.getAmount())){
                        beanSymbolTag.setUpOrDown(true);
                    }else{
                        beanSymbolTag.setUpOrDown(false);
                    }
                    beanSymbolTag.setAmount(String.valueOf(beanRealTime.getAsk()));
                    final int finalI = i;
                    ThreadHelper.instance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMyRecycleAdapter.notifyItemChanged(finalI,"");
                        }
                    });
                    break;
                }
            }
        }
//         diffResult = DiffUtil.calculateDiff(new SymbolTagDiff(mBeanSymbolTags,
//                mDupBeanSymbolTags),true);
//        ThreadHelper.instance().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                diffResult.dispatchUpdatesTo(mMyRecycleAdapter);
//            }
//        });
    }

    class MyRecycleAdapter extends RecyclerView.Adapter<MyHolder> {

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            MyHolder myHolder = new MyHolder(view=LayoutInflater.from(mContext).inflate(R.layout.rv_symbol_tag, parent, false));
            return myHolder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position, List<Object> payloads) {
            if(payloads.isEmpty()){
                onBindViewHolder(holder,position);
            }else{
//                Bundle payload = (Bundle) payloads.get(0);
                holder.mTvSymbolAmount.setText(mBeanSymbolTags.get(position).getAmount());
                if (mBeanSymbolTags.get(position).getUpOrDown()) {
                    holder.mTvSymbolAmount.setTextColor(Color.RED);
                    holder.mIvSymbolIcon.setImageResource(R.mipmap.red);
                } else {
                    holder.mIvSymbolIcon.setImageResource(R.mipmap.green);
                    holder.mTvSymbolAmount.setTextColor(Color.GREEN);
                }
                }
            }


        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {
            if (position == mPosition) {
                holder.mVLink.setBackgroundResource(R.color.backgrount_button_orange);
                holder.mRlTagParent.setBackgroundResource(R.color.link_gray);
            }else{
                holder.mVLink.setBackgroundResource(R.color.link_gray);
                holder.mRlTagParent.setBackground(null);
            }
            holder.mRlTagParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(position);
                    notifyItemChanged(mPosition);
                    mPosition =position;
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
