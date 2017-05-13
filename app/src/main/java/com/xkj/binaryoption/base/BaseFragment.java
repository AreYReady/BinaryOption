package com.xkj.binaryoption.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.xkj.binaryoption.utils.SystemUtil;
import com.xkj.binaryoption.utils.ThreadHelper;

import org.greenrobot.eventbus.EventBus;

import static com.xiasuhuei321.loadingdialog.view.LoadingDialog.Speed.SPEED_TWO;

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

    LoadingDialog loadingDialog;
    public void showDialogLoading(){
        if(loadingDialog==null) {
            loadingDialog = new LoadingDialog(mContext);
            loadingDialog.setLoadingText("加载中")
                    //显示加载成功时的文字
                    .setInterceptBack(true)
                    .setLoadSpeed(SPEED_TWO)
                    .show();
        }
    }
    public void showSucc(final String success){
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(loadingDialog!=null){
                    loadingDialog.setSuccessText(success);
                    loadingDialog.loadSuccess();
                    loadingDialog.close();
                    loadingDialog=null;
                }
            }
        });
    }
    public void showfaild(final String failedString){
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog!=null){
                    loadingDialog.setFailedText(failedString);
                    loadingDialog.loadFailed();
                    loadingDialog=null;
                }
            }
        });
    }
    public void closeDialog(){
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(loadingDialog!=null){
                    loadingDialog.close();
                    loadingDialog=null;
                }
            }
        });

    }

}
