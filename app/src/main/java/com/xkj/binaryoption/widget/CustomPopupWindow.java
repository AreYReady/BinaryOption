package com.xkj.binaryoption.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.adapter.TimePeriodAdapter;
import com.xkj.binaryoption.bean.BeanOrderRequest;
import com.xkj.binaryoption.bean.BeanSymbolConfig;
import com.xkj.binaryoption.bean.RealTimeDataList;
import com.xkj.binaryoption.constant.MyConstant;
import com.xkj.binaryoption.utils.ACache;
import com.xkj.binaryoption.utils.SystemUtil;
import com.xkj.binaryoption.utils.ThreadHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangsc on 2017-04-24.
 * TODO:
 */

public class CustomPopupWindow extends PopupWindow {
    @BindView(R.id.tv_action_title)
    TextView mTvActionTitle;
    @BindView(R.id.tv_current_price)
    TextView mTvCurrentPrice;
    @BindView(R.id.tv_expect_profit)
    TextView mTvExpectProfit;
    @BindView(R.id.b_enter)
    Button mBEnter;
    @BindView(R.id.b_button1)
    Button mBButton1;
    @BindView(R.id.b_button2)
    Button mBButton2;
    @BindView(R.id.b_button3)
    Button mBButton3;
    @BindView(R.id.b_button4)
    Button mBButton4;
    @BindView(R.id.b_button5)
    Button mBButton5;
    @BindView(R.id.b_button6)
    Button mBButton6;
    @BindView(R.id.et_money)
    EditText mEtMoney;
    @BindView(R.id.rv_time_period)
    RecyclerView mRvTimePeriod;
    private String currentPrice = "0";
    private View view;
    private View.OnClickListener mOnClickListener;
    private String TAG = SystemUtil.getTAG(this);
    private BeanSymbolConfig.SymbolsBean mSymbolsBean;
    private MyConstant.BuyAciton buyAciton = MyConstant.BuyAciton.BUY_DOWN;
    private int percent;
    private int cycle;
    private String amount;
    private List<Button> mButtonList;
    private TimePeriodAdapter mTimePeriodAdapter;

    public CustomPopupWindow(final Context context, final BeanSymbolConfig.SymbolsBean symbolsBean, final MyConstant.BuyAciton buyAciton, String currentPrice) {
        this.currentPrice = currentPrice;
        this.mSymbolsBean = symbolsBean;
        this.buyAciton = buyAciton;
        view = LayoutInflater.from(context).inflate(R.layout.v_popuwindow_, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        setContentView(view);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        mButtonList = new ArrayList<>();
        mButtonList.add(mBButton1);
        mButtonList.add(mBButton2);
        mButtonList.add(mBButton3);
        mButtonList.add(mBButton4);
        mButtonList.add(mBButton5);
        mButtonList.add(mBButton6);
        mBButton1.setSelected(true);
        if (buyAciton == MyConstant.BuyAciton.BUY_DOWN) {
            mTvActionTitle.setBackgroundResource(R.color.background_button_green_normal);
            mTvActionTitle.setText(symbolsBean.getSymbol().concat("//买跌"));
            mBEnter.setBackgroundResource(R.drawable.button_green);
//            选择颜色
            for (int x = 0; x < mButtonList.size(); x++) {
                mButtonList.get(x).setBackgroundResource(R.drawable.button_select_green);
            }
        } else {
            mTvActionTitle.setText(symbolsBean.getSymbol().concat("/买涨"));
        }
        int i = 0;
        if (symbolsBean.getCycles() != null) {
            mRvTimePeriod.setAdapter(mTimePeriodAdapter=new TimePeriodAdapter(context,symbolsBean,buyAciton));
            mRvTimePeriod.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            percent = mSymbolsBean.getCycles().get(0).getPercent();
            cycle = mSymbolsBean.getCycles().get(0).getCycle();
            mTimePeriodAdapter.setOnClickListener(new TimePeriodAdapter.OnClickListener() {
                @Override
                public void onClick(int position) {
                    cycle=symbolsBean.getCycles().get(position).getCycle();
                    percent=symbolsBean.getCycles().get(position).getPercent();
                    changeProfit();
                    mTimePeriodAdapter.notifyDataSetChanged();
                }
            });
        }
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float y = motionEvent.getY();
                if (y < view.getHeight() - view.findViewById(R.id.ll_popuwindow_content).getMeasuredHeight()) {
                    dismiss();
                }
                return true;
            }
        });
        amount = mBButton1.getText().toString();
        mEtMoney.setText(amount);
        changeProfit();
        mEtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    amount="0";
                }
                checkMoney(editable.toString().toString());
            }
        });

        mTvCurrentPrice.setText(currentPrice);
        mBEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (symbolsBean.getCycles() == null) {
                    Toast.makeText(context, "当前该种类不能交易", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(amount.equals("")||amount.equals("0")){
                    Toast.makeText(context, "交易金额不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //发送订单
                EventBus.getDefault().post(new BeanOrderRequest(Integer.valueOf(ACache.get(context).getAsString(MyConstant.user_name_mt4))
                        , symbolsBean.getSymbol(), buyAciton == MyConstant.BuyAciton.BUY_DOWN ? 0 : 1
                        , Integer.valueOf(amount), cycle, percent));
            }
        });

    }

    private void checkMoney(String amount) {
        this.amount=amount;
        for(Button button:mButtonList){
            if(button.getText().equals(amount)){
                button.setSelected(true);
            }else{
                button.setSelected(false);
            }
        }
        changeProfit();
    }

    /**
     * 修改收益
     */
    private void changeProfit() {
        ThreadHelper.instance().runOnUiThread(runnable);
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mTvCurrentPrice.setText(currentPrice);
            if (mSymbolsBean.getCycles() != null) {
                mTvExpectProfit.setText((int) (Double.valueOf(amount) * percent / 100) + "元");
            }
        }
    };
    @Subscribe
    public void eventRealTimeData(RealTimeDataList realTimeDataList) {
        for (RealTimeDataList.BeanRealTime beanRealTime : realTimeDataList.getQuotes()) {
            if (beanRealTime.getSymbol().equals(mSymbolsBean.getSymbol())) {
                currentPrice = String.valueOf(beanRealTime.getBid());
                changeProfit();
            }
        }
    }



    @Override
    public void dismiss() {
        EventBus.getDefault().unregister(this);
        super.dismiss();
    }


    @OnClick({R.id.b_button1, R.id.b_button2, R.id.b_button3, R.id.b_button4, R.id.b_button5, R.id.b_button6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.b_button1:
                break;
            case R.id.b_button2:
                break;
            case R.id.b_button3:
                break;
            case R.id.b_button4:
                break;
            case R.id.b_button5:
                break;
            case R.id.b_button6:
                break;

        }
        for(Button button:mButtonList){
            if(button.getId()==view.getId()){
                button.setSelected(true);
            }else{
                button.setSelected(false);
            }
        }
        amount = ((Button) view).getText().toString();
        Log.i(TAG, "onCheckedChanged: " + amount);
         mEtMoney.setText(amount);
    }
}
