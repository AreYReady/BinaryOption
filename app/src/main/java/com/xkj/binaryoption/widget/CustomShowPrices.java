package com.xkj.binaryoption.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.bean.BeanShowPrices;
import com.xkj.binaryoption.utils.DensityUtil;
import com.xkj.binaryoption.utils.SystemUtil;

/**
 * Created by huangsc on 2017-05-02.
 * TODO:
 */

public class CustomShowPrices extends View {
    private float x=0;
    private float y=0;
    private int mHeight=0;
    private int mWidth=0;
    private boolean isShow=false;
    private String TAG= SystemUtil.getTAG(this);
    private Paint mBackPaint;
    private Context mContext;
    private int padding;
    private int layoutPadding;
    private int mWidthPriceSpace;
    private int mHeightPriceSpace;
    private BeanShowPrices mBeanShowPrices;
    public CustomShowPrices(Context context) {
       this(context,null);
    }

    public CustomShowPrices(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomShowPrices(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        init(context,attrs,defStyleAttr);
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
//        mCharHeight=mHeight-mHeight/10;
//        mCharWidth=mWidth-mWidth/10;
//        mTextX=mCharWidth;
//        mTextY=mCharHeight/10;

    }

    /**
     * 初始化
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mBackPaint=new Paint();
        mBackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBackPaint.setStrokeWidth(DensityUtil.dip2px(context,1));
        mBackPaint.setTextSize(DensityUtil.sp2px(mContext,8));
        mBackPaint.setColor(getResources().getColor(R.color.link_gray));
        padding=DensityUtil.dip2px(mContext,5);
        layoutPadding=DensityUtil.dip2px(mContext,15);
        mWidthPriceSpace=DensityUtil.dip2px(mContext,130);
        mHeightPriceSpace=DensityUtil.dip2px(mContext,80);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: ");
         x=event.getX();
         y=event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isShow=true;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: move");
                isShow=true;
                break;
            case MotionEvent.ACTION_UP:
                isShow=false;
                postInvalidate();
                break;
        }
        if(isShow&&mEventXListener!=null){
            mEventXListener.getX(x);
        }
        return true;
    }
    private EventXListener mEventXListener;
    public  interface EventXListener {
    void getX(float x);
}
    public void setEventXListener(EventXListener eventXListener){
        mEventXListener=eventXListener;
    }



    private float pricesStartX =0;
    private float pricesEndX=0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isShow){
            if(x>mWidth/2){
                pricesStartX=x-mWidthPriceSpace;
                pricesEndX=x-layoutPadding;
            }else{
                pricesStartX =x+layoutPadding;
                pricesEndX=x+mWidthPriceSpace;
            }
            Log.i(TAG, "onDraw: ");
            canvas.drawLine(0,y,getWidth(),y,mBackPaint);
            canvas.drawLine(x,0,x,getHeight(),mBackPaint);
            mBackPaint.setColor(Color.BLACK);
            //正常思维
            canvas.drawRoundRect(pricesStartX,y-mHeightPriceSpace-layoutPadding,pricesEndX,y-layoutPadding,(float)5,(float)5,mBackPaint);
            mBackPaint.setColor(getResources().getColor(R.color.text_color_white));
            canvas.drawText("时间："+mBeanShowPrices.getTime(),pricesStartX+padding,y-mHeightPriceSpace-padding,mBackPaint);
            canvas.drawText("开盘价："+mBeanShowPrices.getOpenPrice(),pricesStartX+padding,y-mHeightPriceSpace-padding+1*mHeightPriceSpace/10,mBackPaint);
            canvas.drawText("收盘价："+mBeanShowPrices.getClosePrice(),pricesStartX+padding,y-mHeightPriceSpace-padding+2*mHeightPriceSpace/10,mBackPaint);
            canvas.drawText("最高价："+mBeanShowPrices.getMaxPrice(),pricesStartX+padding,y-mHeightPriceSpace-padding+3*mHeightPriceSpace/10,mBackPaint);
            canvas.drawText("最低价："+mBeanShowPrices.getMinPrices(),pricesStartX+padding,y-mHeightPriceSpace-padding+4*mHeightPriceSpace/10,mBackPaint);
            canvas.drawText("m5值："+mBeanShowPrices.getM5Price(),pricesStartX+padding,y-mHeightPriceSpace-padding+5*mHeightPriceSpace/10,mBackPaint);
            canvas.drawText("m10值："+mBeanShowPrices.getM10Price(),pricesStartX+padding,y-mHeightPriceSpace-padding+6*mHeightPriceSpace/10,mBackPaint);
            canvas.drawText("m20值："+mBeanShowPrices.getM20Price(),pricesStartX+padding,y-mHeightPriceSpace-padding+7*mHeightPriceSpace/10,mBackPaint);
        }
    }
    public void setShowPricesData(BeanShowPrices beanShowPrices){
        mBeanShowPrices=beanShowPrices;
        postInvalidate();
    }
}
