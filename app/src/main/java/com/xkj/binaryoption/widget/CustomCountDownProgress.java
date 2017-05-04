package com.xkj.binaryoption.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xkj.binaryoption.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by huangsc on 2017-05-03.
 * TODO:
 */

public class CustomCountDownProgress extends RelativeLayout {
    private ProgressBar mProgressBar;
    private TextView mTextView;
    public CustomCountDownProgress(Context context) {
        this(context,null);
    }

    public CustomCountDownProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomCountDownProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = LayoutInflater.from(context).inflate(R.layout.v_countdown_progress, null);
        addView(inflate);

    }


}
