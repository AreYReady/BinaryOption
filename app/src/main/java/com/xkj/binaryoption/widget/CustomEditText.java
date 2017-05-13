package com.xkj.binaryoption.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.utils.ThreadHelper;

/**
 * Created by huangsc on 2017-04-17.
 * TODO:
 */

public class CustomEditText extends FrameLayout {
    ImageView mIvEditTextIcon;
    EditText mEtEditText;
    LinearLayout mLlEditTextParent;
    TextView mTvPrompt;
    TextView mTvCheckCode;


    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    private String hint;
    private int color;
    private Bitmap icon;
    private boolean selectStyle=false;
    private int checkCode=GONE;
    private int inputType=-1;
    View inflate;

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CustomEditText_BackgroundColor:
                    color = typedArray.getColor(attr, getResources().getColor(R.color.background_edittext_gray));
                    break;
                case R.styleable.CustomEditText_hint:
                    hint = typedArray.getString(attr);
                    break;
                case R.styleable.CustomEditText_signImage:
                    icon = ((BitmapDrawable) typedArray.getDrawable(attr)).getBitmap();
                    break;
                case R.styleable.CustomEditText_ll_background_style:
                    selectStyle=typedArray.getBoolean(R.styleable.CustomEditText_ll_background_style,false);
                    break;
                case R.styleable.CustomEditText_checkCode:
                    checkCode=typedArray.getInt(R.styleable.CustomEditText_checkCode,GONE);
                case R.styleable.CustomEditText_inputType:
                    inputType=typedArray.getInt(R.styleable.CustomEditText_inputType,-1);
            }
        }
        typedArray.recycle();
         inflate = LayoutInflater.from(context).inflate(R.layout.v_edittext, this);
        mLlEditTextParent=(LinearLayout)inflate.findViewById(R.id.ll_edit_text_parent);
        mEtEditText=(EditText)inflate.findViewById(R.id.et_edit_text);
        if(inputType==0){
            mEtEditText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }else if(inputType==1){
            mEtEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        mIvEditTextIcon=(ImageView)inflate.findViewById(R.id.iv_edit_text_icon);
        mTvPrompt=(TextView)inflate.findViewById(R.id.tv_prompt);
        mTvCheckCode=(TextView)inflate.findViewById(R.id.tv_check_code);
        mIvEditTextIcon.setImageBitmap(icon);
        mEtEditText.setHint(hint);
        mLlEditTextParent.setSelected(selectStyle);
        mTvCheckCode.setVisibility(checkCode);
        //验证码倒计时
        mTvCheckCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnClickCheckCodeListener!=null&&mTvCheckCode.isClickable()){
                    if(mOnClickCheckCodeListener.onClick(view)){
                        countdown();
                        mTvCheckCode.setClickable(false);
                    }
                }

            }
        });
        mEtEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                inflate.setFocusable(b);
            }
        });
    }
    public void setPromptVisibility(int visibility){
        mTvPrompt.setVisibility(visibility);
    }
    public void setPromptText(String desc){
        mTvPrompt.setText(desc);
    }
    public void setText(String desc){
        mEtEditText.setText(desc);
    }
    public String getText(){
        return mEtEditText.getText().toString();
    }
    CountDownTimer cdt;
    int counDownTime=0;
    private void countdown() {
        counDownTime=60;
        cdt = new CountDownTimer(62*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                ThreadHelper.instance().runOnUiThread(runOnUiThread);
            }
            @Override
                public void onFinish() {
                mTvCheckCode.setClickable(true);
                ThreadHelper.instance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvCheckCode.setText("获取验证码");
                    }
                });
            }
        };
        cdt.start();
    }

    Runnable runOnUiThread=new Runnable() {
        @Override
        public void run() {
            mTvCheckCode.setText("剩余"+counDownTime--+"秒");
        }
    };
    public OnClickCheckCodeListener mOnClickCheckCodeListener;
    public void setOnClickCheckCodeListener(OnClickCheckCodeListener onClickCheckCodeListener){
        mOnClickCheckCodeListener=onClickCheckCodeListener;
    }
    public interface OnClickCheckCodeListener{
        boolean onClick(View view);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mEtEditText.setOnFocusChangeListener(l);
    }
}
