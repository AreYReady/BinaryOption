package com.xkj.binaryoption.base;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xkj.binaryoption.R;
import com.xkj.binaryoption.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by huangsc on 2017-04-17.
 * TODO:
 */

public abstract class BaseFragment  extends Fragment {
protected Context mContext;
    protected String TAG= SystemUtil.getTAG(this);
protected View view;
    protected PopupWindow popupWindowLoading;
    private Toast mToast;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
    }

    @Nullable
    @Override
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) ;
    /**
     * 初始化view
     */
    protected abstract void initView();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRegister();
        initData();
        initView();
    }

    protected abstract void initRegister();


    /**
     * 初始化数据
     */
    protected abstract void initData();



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
    /**
     * 显示{@link Toast}通知
     *
     * @param strResId 字符串资源id
     */
    protected void showToast(String strResId) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(strResId);
        mToast.show();
    }
    /**
     * 显示加载中界面
     *
     * @param view 相对view
     */
    public void showPopupLoading(View view) {
        if(popupWindowLoading==null){
            View popView = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
            popupWindowLoading = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            popupWindowLoading.setOutsideTouchable(false);
            popupWindowLoading.setFocusable(false);
            popupWindowLoading.setBackgroundDrawable(new BitmapDrawable());
        }
        popupWindowLoading.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
    public void dissPopupWindos(){
        popupWindowLoading.dismiss();
        popupWindowLoading=null;
    }
}
