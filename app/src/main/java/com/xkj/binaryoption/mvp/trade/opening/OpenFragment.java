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
import com.xkj.binaryoption.utils.DateUtils;
import com.xkj.binaryoption.utils.ThreadHelper;
import com.xkj.binaryoption.utils.ToashUtil;
import com.xkj.binaryoption.widget.CustomKLink;
import com.xkj.binaryoption.widget.CustomPopupWindow;
import com.xkj.binaryoption.widget.CustomTimeLink;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
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
    private Map<String, List<RealTimeDataList.BeanRealTime>> mRealTimeDataMap = new ArrayMap<>();
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
                if(mPeriod.equals("fenshi")) {
                    refreshTimeLink(mRealTimeDataMap);
                }else {
                    sendHistoryPrices();
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
                if (tab.getTag().toString().contains("fenshi")) {
                    mCstContent.setVisibility(View.VISIBLE);
                    mCklContent.setVisibility(View.GONE);
                } else {
                    if (mCstContent.getVisibility() != View.GONE) {
                        mCstContent.setVisibility(View.GONE);
                    }
                    if (mCklContent.getVisibility() != View.VISIBLE) {
                        mCklContent.setVisibility(View.VISIBLE);
                    }
                    sendHistoryPrices();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab.select();
        sendHistoryPrices();
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

    private void sendHistoryPrices() {
        if (mHistoryPricesMap.containsKey(mCurrentSymbol + "_" + DataUtil.selectPeriod(mPeriod))) {
            mCklContent.postInvalidate(mHistoryPricesMap.get(mCurrentSymbol + "_" + DataUtil.selectPeriod(mPeriod)));
        } else {
            mPresenter.sendHistoryPrices(new BeanHistoryRequest(mCurrentSymbol, bar_count, mPeriod));
        }
    }
    private void sendHistoryPrices(RealTimeDataList.BeanRealTime beanRealTime){
        if (mHistoryPricesMap.containsKey(mCurrentSymbol + "_" + DataUtil.selectPeriod(mPeriod))) {
            mCklContent.postInvalidate(mHistoryPricesMap.get(mCurrentSymbol + "_" + DataUtil.selectPeriod(mPeriod)),beanRealTime);
        } else {
            mPresenter.sendHistoryPrices(new BeanHistoryRequest(mCurrentSymbol, bar_count, mPeriod));
        }
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
    private boolean isRefresh = false;

    @Override
    public void eventRealTimeData(RealTimeDataList realTimeDataList) {
        Log.i(TAG, "eventRealTimeData: ");
        refreshSymbolsTag(realTimeDataList);
        refreshRealTimeLink(realTimeDataList);
        refreshKLink(realTimeDataList);
    }

    /**
     * 刷新K线图数据
     *
     * @param realTimeDataList
     */

    private void refreshKLink(RealTimeDataList realTimeDataList) {
        String symbolm5;
        String symbolm15;
        String symbolmh1;
        String symbolmd1;
        for (RealTimeDataList.BeanRealTime beanRealTime : realTimeDataList.getQuotes()) {
            if (mHistoryPricesMap.containsKey(symbolm5 = beanRealTime.getSymbol().concat("_" + DataUtil.selectPeriod("m5")))) {
                //娶最后的时间进行计算，如果还没超过当前时间点，那么时间有效，计算最后一个时间，如果时间超过，删除第一个时间点，增加一个新的时间点

            }
            if (mHistoryPricesMap.containsKey(symbolm15 = beanRealTime.getSymbol().concat("_" + DataUtil.selectPeriod("m15")))) {

            }
            if (mHistoryPricesMap.containsKey(symbolmh1 = beanRealTime.getSymbol().concat("_" + DataUtil.selectPeriod("h1")))) {

            }
            if (mHistoryPricesMap.containsKey(symbolmd1 = beanRealTime.getSymbol().concat("_" + DataUtil.selectPeriod("d1")))) {

            }
            if (!mPeriod.equals("fenshi")&&beanRealTime.getSymbol().equals(mCurrentSymbol)) {
//                是当前商品，刷新k线图
                 sendHistoryPrices(beanRealTime);
            }
        }
    }

    /**
     * 刷新分时图的实施数据
     *
     * @param realTimeDataList
     */
    private void refreshRealTimeLink(RealTimeDataList realTimeDataList) {
        Log.i(TAG, "refreshRealTimeLink: 刷新分时图实时数据");
        for (RealTimeDataList.BeanRealTime beanRealTime : realTimeDataList.getQuotes()) {
            //判断是是否存在，存在则判断是否大于80，不存在则添加
            if (mRealTimeDataMap.containsKey(beanRealTime.getSymbol())) {
                if (mRealTimeDataMap.get(beanRealTime.getSymbol()).size() > 80) {
                    mRealTimeDataMap.get(beanRealTime.getSymbol()).remove(0);
                }
                mRealTimeDataMap.get(beanRealTime.getSymbol()).add(beanRealTime);
            } else {
                LinkedList<RealTimeDataList.BeanRealTime> timeLinkedList = new LinkedList<>();
                timeLinkedList.add(beanRealTime);
                mRealTimeDataMap.put(beanRealTime.getSymbol(), timeLinkedList);
            }
            if (mCurrentSymbol.equals(beanRealTime.getSymbol()))
                isRefresh = true;
        }
        if (isRefresh) {
            refreshTimeLink(mRealTimeDataMap);
            isRefresh = false;
        }
    }

    /**
     * 刷新商品表数据
     *
     * @param realTimeDataList
     */
    private void refreshSymbolsTag(RealTimeDataList realTimeDataList) {
        Log.i(TAG, "refreshSymbolsTag: 刷新商品数据");
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

    /**
     * 刷新分时图
     *
     * @param beanRealTimes
     */
    private void refreshTimeLink(Map<String, List<RealTimeDataList.BeanRealTime>> beanRealTimes) {
        Log.i(TAG, "refreshTimeLink: 刷新分时图");
        mRealTimeDataMap = beanRealTimes;
        //精度存在才继续
        if (mAllSymbolsDigits == null) {
            return;
        }
        //判断是同一个symbol才处理
        mCstContent.postInvalidate(beanRealTimes.containsKey(mCurrentSymbol) ? beanRealTimes.get(mCurrentSymbol) : null,
                isChange, mAllSymbolsDigits.containsKey(mCurrentSymbol) ? mAllSymbolsDigits.get(mCurrentSymbol) : 0);
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
     * 接收历史数据
     *
     * @param beanHistoryPrices
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void eventHistoryPrices(BeanHistoryPrices beanHistoryPrices) {
        Log.i(TAG, "eventHistoryPrices: 历史报价");
        mHistoryPricesMap.put(beanHistoryPrices.getSymbol() + "_" + beanHistoryPrices.getPeriod(), beanHistoryPrices);
        int beginTime=0;
         BeanHistoryPrices.ItemsBean itemsBean;
        for(int i=0;i<mHistoryPricesMap.size();i++){
            if(i!=0){
                beanHistoryPrices.getItems().get(0).setTimeString(DateUtils.getShowTimeNoTimeZone(beginTime+beanHistoryPrices.getItems().get(i).getT(),"MM-dd hh:mm"));
            }else if(i==0){
                beginTime=beanHistoryPrices.getItems().get(0).getT();
                beanHistoryPrices.getItems().get(0).setTimeString(DateUtils.getShowTimeNoTimeZone(beginTime,"MM-dd hh:mm"));
            }
        }
        if (mCurrentSymbol.equals(beanHistoryPrices.getSymbol()) && beanHistoryPrices.getPeriod() == DataUtil.selectPeriod(mPeriod)) {
            mCklContent.postInvalidate(beanHistoryPrices);
        }
    }
}
