package com.xkj.binaryoption.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.View;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.bean.RealTimeDataList;
import com.xkj.binaryoption.constant.MyConstant;
import com.xkj.binaryoption.utils.BigdecimalUtils;
import com.xkj.binaryoption.utils.DensityUtil;
import com.xkj.binaryoption.utils.SystemUtil;

import java.util.List;

/**
 * Created by huangsc on 2017-04-25.
 * TODO:分时图
 */

public class CustomTimeLink extends View {
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
    private Context mContext;
    private Paint mRealTimePaint;
    /**
     * 线图的主题宽高，留右 下 个留空间显示
     */
    private int mLinkWidth;
    private int mLinkHeight;
    /**
     * 是否重新计算
     */
    private boolean isAgain=true;

    public CustomTimeLink(Context context) {
        this(context,null);
    }

    public CustomTimeLink(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomTimeLink(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        init(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }
    public void setWidthHeight(int width, int height) {
        this.mHeight = height;
        this.mWidth = width;
        mLinkHeight =mHeight;
        mLinkWidth =mWidth-mWidth/MyConstant.LINK_FREE_SPACE_W;
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
        mPaintLink.setStrokeWidth(DensityUtil.dip2px(context,2));
        mPaintLink.setColor(getResources().getColor(R.color.background_button_orange_normal));
        mPaintFill =new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(getResources().getColor(R.color.background_edittext_orange_normal));
        mTextPaint=new Paint();
        mTextPaint.setTextSize(DensityUtil.sp2px(context,10));
        mTextPaint.setColor(getResources().getColor(R.color.text_color_white));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mRealTimePaint = new Paint();
        mRealTimePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mRealTimePaint.setColor(getResources().getColor(R.color.text_color_price_fall));
        mRealTimePaint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mData!=null){
            decodeData();
            drawLink(canvas);
            drawText(canvas);
            drawRealPrice(canvas);
        }
    }

    private void drawRealPrice(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        if(mData.size()==1||mData.get(mData.size()-1).getBid()==mData.get(mData.size()-2).getBid()) {
            mRealTimePaint.setColor(getResources().getColor(R.color.text_color_primary_disabled_dark));
        }
        else if(mData.get(mData.size()-1).getBid()>mData.get(mData.size()-2).getBid()) {
            mRealTimePaint.setColor(getResources().getColor(R.color.text_color_price_rise));
        }
        else if(mData.get(mData.size()-1).getBid()<mData.get(mData.size()-2).getBid()) {
            mRealTimePaint.setColor(getResources().getColor(R.color.text_color_price_fall));
        }
        int lastY=Double.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMax, String.valueOf(mData.get(mData.size()-1).getBid())), mHeightUnit)).intValue();
            canvas.drawLine(0,
                    lastY,
                    mLinkWidth,
                    lastY,
                    mRealTimePaint);

            canvas.drawRoundRect(mLinkWidth,
                    lastY+top-DensityUtil.dip2px(mContext,5),
                    mWidth,
                    lastY+bottom+DensityUtil.dip2px(mContext,5),
                    (float)DensityUtil.dip2px(mContext,5),(float) DensityUtil.dip2px(mContext,5),
                    mRealTimePaint
                    );
            canvas.drawRoundRect(mLinkWidth,
                    lastY+top-DensityUtil.dip2px(mContext,5),
                mWidth,
                    lastY+bottom+DensityUtil.dip2px(mContext,5),
                (float)DensityUtil.dip2px(mContext,5),(float) DensityUtil.dip2px(mContext,5),
                    mRealTimePaint
        );
        canvas.drawText(String.valueOf(mData.get(mData.size()-1).getBid())
                , mLinkWidth+(mWidth-mLinkWidth)/2,
                lastY,
                mTextPaint);
    }

    String mPriceTag;
    private void drawText(Canvas canvas) {
        int sub=0;
        int length = BigdecimalUtils.movePointRight(mRange,mDigits).length()    ;
        int first = Integer.valueOf( mRange.subSequence(0, 1).toString());
        if(length==1){
            sub=1;
        } else if(first<2){
            sub=2*(int)Math.pow((double)10,(double)length-2);
        }else if(first<5){
            sub=5*(int)Math.pow((double)10,(double)length-2);
        }else  if(first<10){
            sub=(int)Math.pow((double)10,(double)length-1);
        }
        int residue=Integer.valueOf(BigdecimalUtils.movePointRight(mMax,mDigits))%sub;

        for(int i=0;i<11;i++) {

            //                      (1278.87-0.07)-(0.1)*i
                canvas.drawText(
                        mPriceTag=BigdecimalUtils.movePointLeft(BigdecimalUtils.sub(BigdecimalUtils.sub(BigdecimalUtils.movePointRight(mMax,mDigits),String.valueOf(residue)), String.valueOf(sub*i)),mDigits),
                        mLinkWidth+(mWidth-mLinkWidth)/2,
                        Double.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMax,mPriceTag), mHeightUnit)).intValue(),
                        mTextPaint);
//            Log.i(TAG, "drawText: max "+mMax+"  mPriceTag "+mPriceTag+"    y=="+Double.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMax,mPriceTag), mHeightUnit)).intValue());
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
        pathFill.moveTo(0, mLinkHeight);
        pathFill.lineTo(0, Double.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMax, String.valueOf(mData.get(0).getBid())), mHeightUnit)).intValue());
        for(int i=1;i<mData.size();i++){
            pathLink.lineTo(mLinkWidth * (i ) / mMaxDataSize, Double.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMax, String.valueOf(mData.get(i ).getBid())), mHeightUnit)).intValue());
            pathFill.lineTo(mLinkWidth * (i) / mMaxDataSize, Double.valueOf(BigdecimalUtils.mul(BigdecimalUtils.sub(mMax, String.valueOf(mData.get(i ).getBid())), mHeightUnit)).intValue());
        }
//        pathFill.lineTo();
        pathFill.lineTo(mLinkWidth *(mData.size())/mMaxDataSize, mLinkHeight);
        pathFill.close();
        canvas.drawPath(pathLink, mPaintLink);
        canvas.drawPath(pathFill, mPaintFill);
    }

    private void decodeData() {
        try {
            mHeightUnit =BigdecimalUtils.div(String.valueOf(mLinkHeight),mRange,10);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < mData.size(); i++) {
            if(mData.get(i).getBid()>Double.valueOf(mMax)){
                mMax=String.valueOf(mData.get(i).getBid());
                mMin=BigdecimalUtils.sub(mMedian,BigdecimalUtils.sub(mMax,mMedian));
                mRange=BigdecimalUtils.sub(mMax,mMin);
                //增加最大区间
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
        if(mData!=null) {
            if (this.mData==null || !this.mData.get(0).getSymbol().equals(mData.get(0).getSymbol())) {
                mMedian = String.valueOf(mData.get(0).getBid());
                mMax = BigdecimalUtils.add(mMedian, BigdecimalUtils.movePointLeft("50", digits));
                mMin = BigdecimalUtils.sub(mMedian, BigdecimalUtils.movePointLeft("50", digits));
                mRange = BigdecimalUtils.sub(mMax, mMin);
            }
        }
        this.mData=mData;
        mDigits =digits;
        postInvalidate();
    }
}
