package com.xkj.binaryoption.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xkj.binaryoption.R;

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

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CustomEditText_BackgroundColor:
                    color = typedArray.getColor(attr, getResources().getColor(R.color.backgrount_edittext_gray));
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
            }
        }
        typedArray.recycle();
        View inflate = LayoutInflater.from(context).inflate(R.layout.v_edittext, this);
        mLlEditTextParent=(LinearLayout)inflate.findViewById(R.id.ll_edit_text_parent);
        mEtEditText=(EditText)inflate.findViewById(R.id.et_edit_text);
        mIvEditTextIcon=(ImageView)inflate.findViewById(R.id.iv_edit_text_icon);
        mTvPrompt=(TextView)inflate.findViewById(R.id.tv_prompt);
        mTvCheckCode=(TextView)inflate.findViewById(R.id.tv_check_code);
        mIvEditTextIcon.setImageBitmap(icon);
        mEtEditText.setHint(hint);
        mLlEditTextParent.setSelected(selectStyle);
        mTvCheckCode.setVisibility(checkCode);
    }
    public void setPromptVisibility(int visibility){
        mTvPrompt.setVisibility(visibility);
    }
    public void setPromptText(String desc){
        mTvPrompt.setText(desc);
    }
    public TextView getCheckCode(){
        return mTvCheckCode;
    }
    public void setText(String desc){
        mEtEditText.setText(desc);
    }
    public String getText(){
        return mEtEditText.getText().toString();
    }
}
