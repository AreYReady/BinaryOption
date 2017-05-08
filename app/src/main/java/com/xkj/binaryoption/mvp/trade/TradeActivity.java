package com.xkj.binaryoption.mvp.trade;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseActivity;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.bean.BeanCurrentOrder;
import com.xkj.binaryoption.bean.BeanOrderResult;
import com.xkj.binaryoption.bean.BeanUserInfo;
import com.xkj.binaryoption.message.MessageManageItem;
import com.xkj.binaryoption.mvp.manage.UserManageActivity;
import com.xkj.binaryoption.mvp.trade.opening.OpenFragment;
import com.xkj.binaryoption.mvp.trade.pending.PendFragment;
import com.xkj.binaryoption.widget.BadgeView;
import com.xkj.binaryoption.widget.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangsc on 2017-04-18.
 * TODO:
 */

public class TradeActivity extends BaseActivity {
    @BindView(R.id.iv_uesr)
    ImageView mIvUesr;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.b_deposit)
    Button mBDeposit;
    @BindView(R.id.b_withdraw)
    Button mBWithdraw;
    @BindView(R.id.vp_trade_content)
    NoScrollViewPager mVpTradeContent;
    public static String ALL_SYMBOLS_DATA = "allSymbolsbean";
    @BindView(R.id.tv_order_trade)
    TextView mTvOrderTrade;
    @BindView(R.id.rl_order_trade)
    RelativeLayout mRlOrderTrade;
    @BindView(R.id.tv_order_pending)
    TextView mTvOrderPending;
    @BindView(R.id.rl_order_pending)
    RelativeLayout mRlOrderPending;
    private List<BaseFragment> mFragmentList = new ArrayList<>();
    private String allSymbol;
    private int count = 0;
    private MyPagerAdapter mMyPagerAdapter;
    BadgeView mBadgeView;
    private BeanUserInfo mBeanUserInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_trade);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initRegister() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        allSymbol = getIntent().getStringExtra(ALL_SYMBOLS_DATA);
        OpenFragment openFragment = new OpenFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ALL_SYMBOLS_DATA, allSymbol);
        openFragment.setArguments(bundle);
        mFragmentList.add(openFragment);
        mFragmentList.add(new PendFragment());
        mVpTradeContent.setAdapter(mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager()));
        mVpTradeContent.setNoScroll(true);
        mTvOrderTrade.setTextColor(getResources().getColor(R.color.background_button_orange_normal));
        mBadgeView=new BadgeView(mContext);
        mBadgeView.setTargetView(mTvOrderPending);
        mBadgeView.setBadgeGravity(Gravity.RIGHT|Gravity.TOP);
        mBadgeView.setBadgeCount(count);
        if(mBeanUserInfo!=null)
        mTvAmount.setText(String.valueOf(mBeanUserInfo.getBalance()));
//        mTbOpenOrPend.setupWithViewPager(mVpTradeContent);

//        setUpTabBadge();
    }

    @OnClick({R.id.iv_uesr, R.id.b_deposit, R.id.b_withdraw,R.id.rl_order_trade, R.id.rl_order_pending})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_uesr:
                startActivity(new Intent(this, UserManageActivity.class));
                break;
            case R.id.b_deposit:
                break;
            case R.id.b_withdraw:
                break;
            case R.id.rl_order_trade:
                mVpTradeContent.setCurrentItem(0);
                mTvOrderTrade.setTextColor(getResources().getColor(R.color.background_button_orange_normal));
                mTvOrderPending.setTextColor(getResources().getColor(R.color.text_color_white));
                break;
            case R.id.rl_order_pending:
                mVpTradeContent.setCurrentItem(1);
                mTvOrderPending.setTextColor(getResources().getColor(R.color.background_button_orange_normal));
                mTvOrderTrade.setTextColor(getResources().getColor(R.color.text_color_white));
                break;
        }
    }

    @Subscribe(sticky = true)
    public void eventCurrentOrder(BeanCurrentOrder beanCurrentOrder) {
        count=beanCurrentOrder.getOrders()==null?0:beanCurrentOrder.getOrders().size();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventOrderResult(BeanOrderResult beanOrderResult){
        if(beanOrderResult.getStatus()==0){
            count++;
        }else{
            if(count>0)
                count--;
        }
        if(mBadgeView!=null){
            mBadgeView.setBadgeCount(count);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventManageitem(MessageManageItem messageManageItem){
        Log.i(TAG, "eventManageitem: "+messageManageItem.getItemName());
        switch (messageManageItem.getItemName()){
            case"下单交易":
                mVpTradeContent.setCurrentItem(0);
                mTvOrderTrade.setTextColor(getResources().getColor(R.color.background_button_orange_normal));
                mTvOrderPending.setTextColor(getResources().getColor(R.color.text_color_white));
                break;
            case "持仓记录":
                mVpTradeContent.setCurrentItem(1);
                mTvOrderPending.setTextColor(getResources().getColor(R.color.background_button_orange_normal));
                mTvOrderTrade.setTextColor(getResources().getColor(R.color.text_color_white));
                break;
        }
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventUserInfo(BeanUserInfo beanUserInfo){
        Log.i(TAG, "eventUserInfo: ");
        mBeanUserInfo= beanUserInfo;
        if(mTvAmount!=null){
            mTvAmount.setText(String.valueOf(beanUserInfo.getBalance()));
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
