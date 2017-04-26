package com.xkj.binaryoption.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.bean.RealTimeDataList;
import com.xkj.binaryoption.utils.BigdecimalUtils;
import com.xkj.binaryoption.utils.DensityUtil;
import com.xkj.binaryoption.utils.SystemUtil;

import java.util.List;

/**
 * Created by huangsc on 2017-04-25.
 * TODO:分时图 k线图
 */

public class CustomKLink extends View {
    private Paint mPaintLink;
    private Paint mPaintFill;
    private Paint mTextPaint;
    private SurfaceHolder mSurfaceHolder;
    private List<RealTimeDataList.BeanRealTime> mData;
    private int mWidth;
    private int mHeight;
    private String mMax="0";
    private String mMin="0";
    private String mRange="0";
    private int mMaxDataSize=81;
    private String mMedian="0";
    private String mHeightUnit="0";
    private int mDigits =0;
    private String TAG= SystemUtil.getTAG(this);
    private Path pathLink;
    private Path pathFill;
    private int mTextX=0;
    private int mTextY=0;
    /**
     * 线图的主题宽高，留右 下 个留空间显示
     */
    private int mCharWidth;
    private int mCharHeight;
    /**
     * 是否重新计算
     */
    private boolean isAgain=true;


    public CustomKLink(Context context) {
        this(context,null);
    }

    public CustomKLink(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomKLink(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("ee", "onMeasure-----------");
        setMeasuredDimension(mWidth, mHeight);
    }
    public void setWidthHeight(int width, int height) {
        this.mHeight = height;
        this.mWidth = width;
        mCharHeight=mHeight-mHeight/10;
        mCharWidth=mWidth-mWidth/10;
        mTextX=mCharWidth;
        mTextY=mCharHeight/10;

    }
    /**
     * 初始化
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaintLink =new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLink.setStyle(Paint.Style.STROKE);
        mPaintLink.setStrokeWidth(10);
        mPaintLink.setColor(getResources().getColor(R.color.background_edittext_orange));
        mPaintFill =new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(getResources().getColor(R.color.background_button_green));
        mTextPaint=new Paint();
        mTextPaint.setTextSize(DensityUtil.sp2px(context,10));
        mTextPaint.setColor(getResources().getColor(R.color.text_color_white));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mData!=null){
            decodeData();
            drawLink(canvas);
            drawText(canvas);
        }
    }

    private void drawText(Canvas canvas) {

        for(int i=1;i<11;i++) {
            try {
                canvas.drawText("-"+BigdecimalUtils.sub(mMax,BigdecimalUtils.mul(BigdecimalUtils.div(mRange,"10",mDigits),""+i)), mTextX,mTextY*i,mTextPaint);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 划线
     * @param canvas
     */
    private void drawLink(Canvas canvas) {
        pathLink=new Path();
        pathFill=new Path();
        pathLink.moveTo(0, Double.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMax, String.valueOf(mData.get(0).getBid())), mHeightUnit)).intValue());
        pathFill.moveTo(0,mCharHeight);
        pathFill.lineTo(0, Double.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMax, String.valueOf(mData.get(0).getBid())), mHeightUnit)).intValue());
        for(int i=1;i<mData.size();i++){
//            Log.i(TAG, "drawLink: ");
//            Log.i(TAG, "drawLink: h"+Double.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMax, String.valueOf(mData.get(i + 1).getBid())), mHeightUnit)).intValue());
//            Log.i(TAG, "drawLink: w"+mWidth * (i + 1) / mMaxDataSize);
            pathLink.lineTo(mCharWidth * (i ) / mMaxDataSize, Double.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMax, String.valueOf(mData.get(i ).getBid())), mHeightUnit)).intValue());
            //先这样，填充颜色后面做
            pathFill.lineTo(mCharWidth * (i) / mMaxDataSize, Double.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMax, String.valueOf(mData.get(i ).getBid())), mHeightUnit)).intValue());
//            pathFill.lineTo(mCharWidth * (i + 1) / mMaxDataSize,mCharHeight);
        }
        pathFill.setLastPoint(mCharWidth*(mData.size())/mMaxDataSize,mCharHeight);
        pathFill.close();
        canvas.drawPath(pathLink, mPaintLink);
        canvas.drawPath(pathFill, mPaintFill);
    }

    private void decodeData() {
            try {
                mHeightUnit =BigdecimalUtils.div(String.valueOf(mCharHeight),mRange,10);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < mData.size(); i++) {
                if(mData.get(i).getBid()>Double.valueOf(mMax)){
                    mMax=String.valueOf(mData.get(i).getBid());
                    mMin=BigdecimalUtils.sub(mMedian,BigdecimalUtils.sub(mMax,mMedian));
                    mRange=BigdecimalUtils.sub(mMax,mMin);
                    break;
                }
                if(mData.get(i).getBid()<Double.valueOf(mMin)){
                    mMin=String.valueOf(mData.get(i).getBid());
                    mMax=BigdecimalUtils.add(mMedian,BigdecimalUtils.sub(mMedian,mMin));
                    mRange=BigdecimalUtils.sub(mMax,mMin);
                    break;
                }
            }
        }

public void postInvalidate(List<RealTimeDataList.BeanRealTime> mData,boolean isFirst,int digits){
        if(mMax.equals("0")||!this.mData.get(0).getSymbol().equals(mData.get(0).getSymbol())) {
            mMedian=String.valueOf(mData.get(0).getBid());
            mMax = BigdecimalUtils.add(mMedian, BigdecimalUtils.movePointLeft("50", digits));
            mMin = BigdecimalUtils.sub(mMedian, BigdecimalUtils.movePointLeft("50", digits));
            mRange = BigdecimalUtils.sub(mMax, mMin);
        }
        this.mData=mData;
        mDigits =digits;
        postInvalidate();
}
}
