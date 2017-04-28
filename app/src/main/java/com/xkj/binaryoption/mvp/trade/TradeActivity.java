package com.xkj.binaryoption.mvp.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.base.BaseActivity;
import com.xkj.binaryoption.base.BaseFragment;
import com.xkj.binaryoption.mvp.trade.opening.OpenFragment;
import com.xkj.binaryoption.mvp.trade.pending.PendFragment;

import org.greenrobot.eventbus.EventBus;

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
    @BindView(R.id.tb_open_or_pend)
    TabLayout mTbOpenOrPend;
    @BindView(R.id.vp_trade_content)
    ViewPager mVpTradeContent;
    public static String ALL_SYMBOLS_DATA="allSymbolsbean";
    private List<BaseFragment> mFragmentList=new ArrayList<>();
    private List<String> mStrings=new ArrayList<>();
    private String allSymbol;


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
        OpenFragment openFragment=new OpenFragment();
        Bundle bundle=new Bundle();
        bundle.putString(ALL_SYMBOLS_DATA,allSymbol);
        openFragment.setArguments(bundle);
        mFragmentList.add(openFragment);
        mFragmentList.add(new PendFragment());
        mStrings.add("下单交易");
        mStrings.add("持仓记录");
        mVpTradeContent.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTbOpenOrPend.setupWithViewPager(mVpTradeContent);

    }

    @OnClick({R.id.iv_uesr, R.id.b_deposit, R.id.b_withdraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_uesr:
                break;
            case R.id.b_deposit:
                break;
            case R.id.b_withdraw:
               break;
        }
    }
   public class MyPagerAdapter extends FragmentPagerAdapter{
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

       @Override
       public CharSequence getPageTitle(int position) {
           return mStrings.get(position);
       }
   }
}
