package com.xkj.binaryoption.mvp.trade.opening;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xkj.binaryoption.R;
import com.xkj.binaryoption.adapter.SymbolsTagAdapter;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.bean.BeanHistoryPrices;
import com.xkj.binaryoption.bean.BeanHistoryRequest;
import com.xkj.binaryoption.bean.BeanOrderResponse;
import com.xkj.binaryoption.bean.BeanSymbolConfig;
import com.xkj.binaryoption.bean.BeanSymbolTag;
import com.xkj.binaryoption.bean.RealTimeDataList;
import com.xkj.binaryoption.constant.MyConstant;
import com.xkj.binaryoption.mvp.trade.TradeActivity;
import com.xkj.binaryoption.mvp.trade.opening.contract.OpenContract;
import com.xkj.binaryoption.mvp.trade.opening.presenter.OpenPresenterImpl;
import com.xkj.binaryoption.utils.DataUtil;
import com.xkj.binaryoption.utils.ThreadHelper;
import com.xkj.binaryoption.utils.ToashUtil;
import com.xkj.binaryoption.widget.CustomKLink;
import com.xkj.binaryoption.widget.CustomPopupWindow;
import com.xkj.binaryoption.widget.CustomTimeLink;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by huangsc on 2017-04-18.
 * TODO:
 */

public class OpenFragment extends BaseFragment implements OpenContract.View {

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
    @BindView(R.id.cst_content)
    CustomTimeLink mCstContent;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.ckl_content)
    CustomKLink mCklContent;
    private List<BeanSymbolTag> mBeanSymbolTags;
    private List<BeanSymbolTag> mDupBeanSymbolTags;
    private String mAllSubSymbols;
    BeanSymbolConfig beanSymbolConfig;
    private OpenContract.Presenter mPresenter;
    private final String AMOUNT_CHANG = "amountChang";
    private int mPosition = 0;
    CustomPopupWindow customPopupWindow;
    private boolean isChange = false;
    private String mCurrentSymbol = "";
    private String mPeriod = "m5";
    private int bar_count = 60;
    private Map<String, Integer> mAllSymbolsDigits;
    private Map<String, List<RealTimeDataList.BeanRealTime>> mRealTimeDataMap;
    private Map<String, BeanHistoryPrices> mHistoryPricesMap = new ArrayMap<>();

    /**
     * 记录实时分时图数据/秒
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_opening, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    SymbolsTagAdapter mMyRecycleAdapter;

    @Override
    protected void initView() {
        mRvSymbols.setAdapter(mMyRecycleAdapter = new SymbolsTagAdapter(mContext, mBeanSymbolTags));
        mMyRecycleAdapter.setOnItemClickListener(new SymbolsTagAdapter.OnItemClickListener() {

            @Override
            public void onClick(int position, String symbols) {
                mPosition = position;
                setCurrentSymbol(mBeanSymbolTags.get(position).getSymbol());
                eventRealTimeChar(mRealTimeDataMap);
                if(mHistoryPricesMap.containsKey(symbols+"_"+DataUtil.selectPeriod(mPeriod))){
                    mCklContent.postInvalidate(mHistoryPricesMap.get(symbols+"_"+DataUtil.selectPeriod(mPeriod)));
                }else{
                    mPresenter.sendHistoryPrices(new BeanHistoryRequest(symbols,bar_count,mPeriod));
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvSymbols.setLayoutManager(layoutManager);
        mTlType.addTab(mTlType.newTab().setText("分时图").setTag("fenshi"));
        TabLayout.Tab tab;
        mTlType.addTab(tab = mTlType.newTab().setText("5分钟").setTag("m5"));
        mTlType.addTab(mTlType.newTab().setText("15分钟").setTag("m15"));
        mTlType.addTab(mTlType.newTab().setText("60分钟").setTag("h1"));
        mTlType.addTab(mTlType.newTab().setText("日线").setTag("d1"));
        LinearLayout linearLayout = (LinearLayout) mTlType.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(mContext,
                R.drawable.layout_divider_vertical));
        mScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mCstContent.setWidthHeight(mScrollView.getWidth(), mScrollView.getHeight());
                mCklContent.setWidthHeight(mScrollView.getWidth(), mScrollView.getHeight());
                Log.i(TAG, "initView: " + mScrollView.getWidth());
            }
        });
        setCurrentSymbol(mBeanSymbolTags.get(0).getSymbol());
        mTlType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPeriod = tab.getTag().toString();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab.select();
        mPresenter.sendHistoryPrices(new BeanHistoryRequest(mCurrentSymbol, 60, mPeriod));
    }

    @Override
    protected void initRegister() {
        mPresenter = new OpenPresenterImpl(mContext, this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        mBeanSymbolTags = new ArrayList<>();
        mDupBeanSymbolTags = new ArrayList<>();
        mAllSubSymbols = getArguments().getString(TradeActivity.ALL_SYMBOLS_DATA);
        beanSymbolConfig = new Gson().fromJson(mAllSubSymbols, BeanSymbolConfig.class);
        for (BeanSymbolConfig.SymbolsBean symbolsBean : beanSymbolConfig.getSymbols()) {
            mBeanSymbolTags.add(new BeanSymbolTag(symbolsBean.getDesc(), symbolsBean.getSymbol(), "0.0", true));
            mDupBeanSymbolTags.add(new BeanSymbolTag(symbolsBean.getDesc(), symbolsBean.getSymbol(), "0.0", true));
            mPresenter.sendSubSymbol(symbolsBean.getSymbol());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void showPopFormBottom(MyConstant.BuyAciton buyAciton) {
        Log.i(TAG, "showPopFormBottom: ");
//        MyHolder childViewHolder = (SymbolsTagAdapter.MyHolder) mRvSymbols.getChildViewHolder(mRvSymbols.getChildAt(mPosition));
        customPopupWindow = new CustomPopupWindow(mContext, beanSymbolConfig.getSymbols().get(mPosition), buyAciton, mBeanSymbolTags.get(mPosition).getAmount());
        customPopupWindow.showAtLocation(this.view, Gravity.CENTER, 0, 0);
    }


    @OnClick({R.id.b_up, R.id.b_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.b_up:
                showPopFormBottom(MyConstant.BuyAciton.BUY_UP);
                break;
            case R.id.b_down:
                showPopFormBottom(MyConstant.BuyAciton.BUY_DOWN);
                break;
        }
    }

    /**
     * 获取实时数据
     *
     * @param realTimeDataList
     */
    DiffUtil.DiffResult diffResult;

    /**
     * 刷新实时数据
     *
     * @param realTimeDataList
     */
    @Override
    public void eventRealTimeData(RealTimeDataList realTimeDataList) {
        Log.i(TAG, "eventRealTimeData: ");
        BeanSymbolTag beanSymbolTag;
        for (RealTimeDataList.BeanRealTime beanRealTime : realTimeDataList.getQuotes()) {

            for (int i = 0; i < mBeanSymbolTags.size(); i++) {
                beanSymbolTag = mBeanSymbolTags.get(i);
                if (beanSymbolTag.getSymbol().equals(beanRealTime.getSymbol())) {
                    if (beanRealTime.getAsk() > Double.valueOf(beanSymbolTag.getAmount())) {
                        beanSymbolTag.setUpOrDown(true);
                    } else {
                        beanSymbolTag.setUpOrDown(false);
                    }
                    beanSymbolTag.setAmount(String.valueOf(beanRealTime.getAsk()));
                    final int finalI = i;
                    ThreadHelper.instance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMyRecycleAdapter.notifyItemChanged(finalI, "amount");
                        }
                    });
                    break;
                }
            }
        }
    }

    @Override
    public void eventRealTimeChar(Map<String, List<RealTimeDataList.BeanRealTime>> beanRealTimes) {
        Log.i(TAG, "eventRealTimeChar: 刷新实时数据");
        mRealTimeDataMap = beanRealTimes;
        //精度存在才继续
        if (mAllSymbolsDigits == null) {
            return;
        }
        //判断是同一个symbol才处理
        mCstContent.postInvalidate(beanRealTimes.get(mCurrentSymbol), isChange, mAllSymbolsDigits.get(mCurrentSymbol));
    }

    @Override
    public void eventAllSymbolsData(Map<String, Integer> mAllSymbolsDigits) {
        this.mAllSymbolsDigits = mAllSymbolsDigits;
    }


    /**
     * 下单结果
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventOrderResponse(BeanOrderResponse beanOrderResponse) {
        if (beanOrderResponse.getResult_code() == 0) {
            ToashUtil.showShort(mContext, "下单成功");
            if (customPopupWindow != null) {
                customPopupWindow.dismiss();
                customPopupWindow = null;
            }
        } else {
            ToashUtil.showShort(mContext, "下单失败，请重新");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setCurrentSymbol(String currentSymbol) {
        mCurrentSymbol = currentSymbol;
        mPresenter.setCurrentSymbol(mCurrentSymbol);
    }

    /**
     * 接受历史数据
     *
     * @param beanHistoryPrices
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void eventHistoryPrices(BeanHistoryPrices beanHistoryPrices) {
        Log.i(TAG, "eventHistoryPrices: 历史报价");
        mHistoryPricesMap.put(beanHistoryPrices.getSymbol() + "_" + beanHistoryPrices.getPeriod(), beanHistoryPrices);
        if(mCurrentSymbol.equals(beanHistoryPrices.getSymbol())&&beanHistoryPrices.getPeriod()== DataUtil.selectPeriod(mPeriod)){
            mCklContent.postInvalidate(beanHistoryPrices);
        }
    }
}
