package com.xkj.binaryoption.widget;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.bean.BeanOrderRequest;
import com.xkj.binaryoption.bean.BeanSymbolConfig;
import com.xkj.binaryoption.bean.RealTimeDataList;
import com.xkj.binaryoption.constant.MyConstant;
import com.xkj.binaryoption.utils.ACache;
import com.xkj.binaryoption.utils.DensityUtil;
import com.xkj.binaryoption.utils.SystemUtil;
import com.xkj.binaryoption.utils.ThreadHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huangsc on 2017-04-24.
 * TODO:
 */

public class CustomPopupWindow extends PopupWindow{
    @BindView(R.id.tv_action_title)
    TextView mTvActionTitle;
    @BindView(R.id.rg_amount)
    RadioGroup mRgAmount;
    @BindView(R.id.rg_time_period)
    RadioGroup mRgTimePeriod;
    @BindView(R.id.tv_current_price)
    TextView mTvCurrentPrice;
    @BindView(R.id.tv_expect_profit)
    TextView mTvExpectProfit;
    @BindView(R.id.b_enter)
    Button mBEnter;
    private String currentPrice="0";
    private View view;
    private View.OnClickListener mOnClickListener;
    private String TAG = SystemUtil.getTAG(this);
    private BeanSymbolConfig.SymbolsBean mSymbolsBean;
    private MyConstant.BuyAciton buyAciton= MyConstant.BuyAciton.BUY_DOWN;
    private int percent;
    private int cycle;
    private String amount;
    public CustomPopupWindow(final Context context, final BeanSymbolConfig.SymbolsBean symbolsBean, final MyConstant.BuyAciton buyAciton, String currentPrice) {
        this.currentPrice=currentPrice;
        this.mSymbolsBean =symbolsBean;
        this.buyAciton=buyAciton;
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
        if (buyAciton == MyConstant.BuyAciton.BUY_DOWN) {
            mTvActionTitle.setBackgroundResource(R.color.background_button_green_normal);
            mTvActionTitle.setText(symbolsBean.getSymbol().concat("//买跌"));
            mBEnter.setBackgroundResource(R.drawable.button_green);
            for (int x = 0; x < mRgAmount.getChildCount(); x++) {
                mRgAmount.getChildAt(x).setBackgroundResource(R.drawable.button_select_green);
            }
        } else {
            mTvActionTitle.setText(symbolsBean.getSymbol().concat("/买涨"));
        }
        int i = 0;
        if(symbolsBean.getCycles()!=null) {
            for (BeanSymbolConfig.SymbolsBean.CyclesBean cyclesBean : symbolsBean.getCycles()) {
                RadioButton radioButton = new RadioButton(context, null);
                radioButton.setText(cyclesBean.getDesc());
                radioButton.setTextAppearance(context, R.style.text_desc_small);
                radioButton.setGravity(Gravity.CENTER);
                radioButton.setTag(R.id.tag_first, cyclesBean.getPercent());
                radioButton.setTag(R.id.tag_second, cyclesBean.getCycle());
                radioButton.setPadding(0, 0, 0, 0);
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(DensityUtil.dip2px(context, 40), DensityUtil.dip2px(context, 25));
                layoutParams.setMargins(0, 0, DensityUtil.dip2px(context, 5), 0);
                radioButton.setLayoutParams(layoutParams);
                radioButton.setButtonDrawable(null);
                if (buyAciton == MyConstant.BuyAciton.BUY_DOWN) {
                    radioButton.setBackgroundResource(R.drawable.button_select_green);
                } else {
                    radioButton.setBackgroundResource(R.drawable.button_select_orange);
                }
                setOutsideTouchable(true);
                mRgTimePeriod.addView(radioButton);
                if (i == 0) {
                    mRgTimePeriod.check(radioButton.getId());
                    i++;
                }
            }
            percent=mSymbolsBean.getCycles().get(0).getPercent();
            cycle=mSymbolsBean.getCycles().get(0).getCycle();
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
        amount=mRgAmount.getChildAt(0).getTag().toString();

        changeProfit();
        mRgAmount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                amount=radioGroup.findViewById(i).getTag().toString();
                Log.i(TAG, "onCheckedChanged: "+amount);
                changeProfit();
            }
        });

        mTvCurrentPrice.setText(currentPrice);
        mRgTimePeriod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                percent=(int)radioGroup.findViewById(i).getTag(R.id.tag_first);
                cycle=(int)radioGroup.findViewById(i).getTag(R.id.tag_second);
                Log.i(TAG, "onCheckedChanged: "+amount);
                changeProfit();
            }
        });
        mBEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(symbolsBean.getCycles()==null){
                    Toast.makeText(context,"当前该种类不能交易",Toast.LENGTH_SHORT).show();
                    return;
                }
                //发送订单
                EventBus.getDefault().post(new BeanOrderRequest(Integer.valueOf(ACache.get(context).getAsString(MyConstant.user_name))
                        ,symbolsBean.getSymbol(),buyAciton== MyConstant.BuyAciton.BUY_DOWN?0:1
                        ,Integer.valueOf(amount),cycle,percent));
            }
        });
    }
    /**
     * 修改收益
     */
    private void changeProfit() {
        ThreadHelper.instance().runOnUiThread(runnable);
    }

    @Subscribe
    public void eventRealTimeData(RealTimeDataList realTimeDataList){
        for(RealTimeDataList.BeanRealTime beanRealTime:realTimeDataList.getQuotes()){
            if(beanRealTime.getSymbol().equals(mSymbolsBean.getSymbol())){
                currentPrice=String.valueOf(beanRealTime.getBid());
                changeProfit();
            }
        }
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            mTvCurrentPrice.setText(currentPrice);
            if(mSymbolsBean.getCycles()!=null) {
                mTvExpectProfit.setText((int) (Double.valueOf(amount) * percent / 100) + "元");
            }
        }
    };

    @Override
    public void dismiss() {
        EventBus.getDefault().unregister(this);
        super.dismiss();
    }


}
