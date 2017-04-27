package com.xkj.binaryoption.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.bean.BeanHistoryPrices;
import com.xkj.binaryoption.bean.RealTimeDataList;
import com.xkj.binaryoption.constant.MyConstant;
import com.xkj.binaryoption.utils.BigdecimalUtils;
import com.xkj.binaryoption.utils.DensityUtil;
import com.xkj.binaryoption.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangsc on 2017-04-27.
 * TODO:k线图
 */

public class CustomKLink extends View {
    private String TAG = SystemUtil.getTAG(this);
    private Paint mfallPaint;
    private Context mContext;
    private Paint mRisePaint;
    private Paint mGarkPaint;
    private Paint mSilverPaint;
    private Paint mDashedPaint;
    private Paint mTempPaint;
    private Paint mMeanPaint;
    private int mWidth = 0;
    private int mHeight = 0;
    private int mLinkWidth = 0;
    private int mLinkHeight = 0;
    private String mCurrentSymbols = "";
    private BeanHistoryPrices mBeanHistoryPrices;
    private String mWidthUnit;
    private String mHeightUnit;
    private String mMaxPrice = "0";
    private String mMinPrice;
    private String mHeightRang = "0";
    private int begin = 20;
    private RealTimeDataList.BeanRealTime mBeanRaRealTime;

    public CustomKLink(Context context) {
        this(context, null);
    }

    public CustomKLink(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomKLink(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure-----------");
        setMeasuredDimension(mWidth, mHeight);
    }

    public void setWidthHeight(int width, int height) {
        this.mHeight = height;
        this.mWidth = width;
        mLinkHeight = mHeight - mHeight / MyConstant.LINK_FREE_SPACE_H;
        mLinkWidth = mWidth - mWidth / MyConstant.LINK_FREE_SPACE_W;
//        mCharHeight=mHeight-mHeight/10;
//        mCharWidth=mWidth-mWidth/10;
//        mTextX=mCharWidth;
//        mTextY=mCharHeight/10;

    }

    private void init() {
        mfallPaint = new Paint();
        mfallPaint.setStyle(Paint.Style.FILL);
        mfallPaint.setColor(getResources().getColor(R.color.text_color_price_fall));
        mfallPaint.setStrokeWidth(3);
        mRisePaint = new Paint();
        mRisePaint.setStyle(Paint.Style.FILL);
        mRisePaint.setColor(getResources().getColor(R.color.text_color_price_rise));
        mRisePaint.setStrokeWidth(3);
        mSilverPaint = new Paint();
        mSilverPaint.setStyle(Paint.Style.STROKE);
        mSilverPaint.setColor(getResources().getColor(R.color.text_color_primary_disabled_dark));
        mSilverPaint.setStrokeWidth(3);
        mGarkPaint = new Paint();
        mGarkPaint.setColor(getResources().getColor(R.color.text_color_primary_dark_with_opacity));
        mGarkPaint.setStrokeWidth(3);
        mGarkPaint.setTextAlign(Paint.Align.CENTER);
        mGarkPaint.setTextSize(DensityUtil.dip2px(mContext, 10));
        mDashedPaint = new Paint();
        mDashedPaint.setStyle(Paint.Style.FILL);
        mDashedPaint.setStrokeWidth(3);

        mMeanPaint = new Paint();
        mMeanPaint.setStrokeWidth(3);
        mMeanPaint.setStyle(Paint.Style.STROKE);
    }

    public void postInvalidate(BeanHistoryPrices beanHistoryPrices) {
        mBeanHistoryPrices = beanHistoryPrices;
        postInvalidate();
    }
    public void postInvalidate(BeanHistoryPrices beanHistoryPrices, RealTimeDataList.BeanRealTime beanRealTime) {
        mBeanHistoryPrices = beanHistoryPrices;
        mBeanRaRealTime=beanRealTime;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBeanHistoryPrices != null) {
            decodeData();
            drawRect(canvas);
            drawText(canvas);
        }

    }

    /**
     * 文字
     * @param canvas
     */
    private void drawText(Canvas canvas) {
            if(Integer.valueOf(mHeightRang)<50){
                
            }else{

            }
    }

    private void drawRect(Canvas canvas) {
        BeanHistoryPrices.ItemsBean itemsBean;
        String[] split;
        String startY;
        String endY;
        String startOpenY;
        String endOpenY;
        float linkX;
        float left;
        float right;
        String top;
        String bottom;

        float yTop;
        float yBottom;
        int m5 = 0;
        int m10 = 0;
        int m20 = 0;
        Path path5 = new Path();
        Path path10 = new Path();
        Path path20 = new Path();
        List<String> mClosePriceList = new ArrayList<>();
        String meanPrice = "0";
        for (int i = 0; i < mBeanHistoryPrices.getCount(); i++) {
            itemsBean = mBeanHistoryPrices.getItems().get(i);
            split = itemsBean.getO().split("\\|");
            if (mClosePriceList.size() >= 20) {
                mClosePriceList.remove(0);
            }
            mClosePriceList.add(BigdecimalUtils.add(split[0], split[3]));

            if (i < begin) {
                continue;
            }
            try {
                //画m5线
                //每次进来都需要清0
                    meanPrice="0";
                    for (int x = 0; x < 5; x++) {
                        meanPrice = BigdecimalUtils.add(meanPrice, mClosePriceList.get(mClosePriceList.size()-x-1));
                    }
                    meanPrice = BigdecimalUtils.div(meanPrice, "5", 0);
                        if (path5.isEmpty()) {
                            path5.moveTo(0, yTop=Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, meanPrice), mHeightUnit))));
                            path5.lineTo(Float.valueOf(mWidthUnit) / 2, yBottom=Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, meanPrice), mHeightUnit))));
                        } else {
//                            path5.lineTo(mWidth,mHeight);
                            path5.lineTo(yBottom=Float.valueOf(mWidthUnit) / 2 + Float.valueOf(mWidthUnit) * (i-begin), yTop=Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, meanPrice), mHeightUnit))));
                        }
                //画10线
                meanPrice="0";
                for (int x = 0; x < 10; x++) {
                    meanPrice = BigdecimalUtils.add(meanPrice, mClosePriceList.get(mClosePriceList.size()-x-1));
                }
                meanPrice = BigdecimalUtils.div(meanPrice, "10", 0);
                if (path10.isEmpty()) {
                    path10.moveTo(0, yTop=Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, meanPrice), mHeightUnit))));
                    path10.lineTo(Float.valueOf(mWidthUnit) / 2, yBottom=Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, meanPrice), mHeightUnit))));
                } else {
//                            path5.lineTo(mWidth,mHeight);
                    path10.lineTo(yBottom=Float.valueOf(mWidthUnit) / 2 + Float.valueOf(mWidthUnit) * (i-begin), yTop=Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, meanPrice), mHeightUnit))));
                }
                //画20线
                meanPrice="0";
                for (int x = 0; x < 20; x++) {
                    meanPrice = BigdecimalUtils.add(meanPrice, mClosePriceList.get(mClosePriceList.size()-x-1));
                }
                meanPrice = BigdecimalUtils.div(meanPrice, "20", 0);
                if (path20.isEmpty()) {
                    path20.moveTo(0, yTop=Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, meanPrice), mHeightUnit))));
                    path20.lineTo(Float.valueOf(mWidthUnit) / 2, yBottom=Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, meanPrice), mHeightUnit))));
                } else {
//                            path5.lineTo(mWidth,mHeight);
                    path20.lineTo(yBottom=Float.valueOf(mWidthUnit) / 2 + Float.valueOf(mWidthUnit) * (i-begin), yTop=Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, meanPrice), mHeightUnit))));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (Double.valueOf(split[3]) > 0) {
                mTempPaint = mRisePaint;
            } else {
                mTempPaint = mfallPaint;
            }
            //画最高点线，和最低点线
            startY = BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, BigdecimalUtils.add(split[0], split[1])), mHeightUnit);
            endY = BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, BigdecimalUtils.add(split[0], split[2])), mHeightUnit);
            canvas.drawLine(linkX = Float.valueOf(mWidthUnit) / 2 + Float.valueOf(mWidthUnit) * (i-begin), Float.valueOf(startY), linkX, Float.valueOf(endY), mTempPaint);
            //画开仓和关仓矩形
            if (Double.valueOf(split[3]) < 0) {
                canvas.drawRect(left = Float.valueOf(mWidthUnit) * (i-begin) + Float.valueOf(mWidthUnit) / 6, yTop = Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, split[0]), mHeightUnit))),
                        right = Float.valueOf(mWidthUnit) * (i-begin) + 5 * Float.valueOf(mWidthUnit) / 6, yBottom = Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, BigdecimalUtils.add(split[0], split[3])), mHeightUnit))), mTempPaint);
            } else {
                canvas.drawRect(left = Float.valueOf(mWidthUnit) * (i-begin) + Float.valueOf(mWidthUnit) / 6, yTop = Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, BigdecimalUtils.add(split[0], split[3])), mHeightUnit))),
                        right = Float.valueOf(mWidthUnit) * (i-begin) + 5 * Float.valueOf(mWidthUnit) / 6, yBottom = Math.abs(Float.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMaxPrice, split[0]), mHeightUnit))), mTempPaint);
            }

        }
        mMeanPaint.setColor(getResources().getColor(R.color.link_m5));
        canvas.drawPath(path5,mMeanPaint);
        mMeanPaint.setColor(getResources().getColor(R.color.link_m10));
        canvas.drawPath(path10,mMeanPaint);
        mMeanPaint.setColor(getResources().getColor(R.color.link_m20));
        canvas.drawPath(path20,mMeanPaint);

    }

    /**
     * 计算必要数据
     */
    private void decodeData() {
        BeanHistoryPrices.ItemsBean itemsBean;
        String[] split;
        String indexMax;
        String indexMin;
        for (int i = 0; i < mBeanHistoryPrices.getCount(); i++) {
            itemsBean = mBeanHistoryPrices.getItems().get(i);
            split = itemsBean.getO().split("\\|");
            if (i == 0) {
                mMaxPrice = BigdecimalUtils.add(split[0], split[1]);
                mMinPrice = BigdecimalUtils.add(split[0], split[2]);
            }
            if (Double.valueOf(mMaxPrice) < Double.valueOf(indexMax = BigdecimalUtils.add(split[0], split[1]))) {
                mMaxPrice = indexMax;
            }
            if (Double.valueOf(mMinPrice) > Double.valueOf(indexMin = BigdecimalUtils.add(split[0], split[2]))) {
                mMinPrice = indexMin;
            }
        }
        //为了让土块在中间，扩大最大最小值
        mHeightRang = BigdecimalUtils.sub(mMaxPrice, mMinPrice);
        mMaxPrice = BigdecimalUtils.add(mMaxPrice, String.valueOf(Integer.valueOf(mHeightRang) / 4));
        mMinPrice = BigdecimalUtils.sub(mMinPrice, String.valueOf(Integer.valueOf(mHeightRang) / 4));
        mHeightRang = BigdecimalUtils.sub(mMaxPrice, mMinPrice);
        try {
            mHeightUnit = BigdecimalUtils.div(String.valueOf(mLinkHeight), String.valueOf(mHeightRang), mBeanHistoryPrices.getDigits());
            mWidthUnit = BigdecimalUtils.div("" + mLinkWidth, "" + (mBeanHistoryPrices.getCount() - begin), mBeanHistoryPrices.getDigits());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
