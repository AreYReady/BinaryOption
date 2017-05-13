package com.xkj.binaryoption.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.xkj.binaryoption.R;

/**
 * Created by huangsc on 2017-03-06.
 * TODO:
 */

public  class DialogUtils {
    public AlertDialog.Builder alertDialog;

    public DialogUtils(Context context, String title, String msg,
                       String positiveName, DialogInterface.OnClickListener positiveListener,
                       String negativeName, DialogInterface.OnClickListener negativeListener) {
        initDialog(context, title, msg, positiveName, positiveListener, negativeName, negativeListener);
    }
    public DialogUtils(Context context, String title, String msg,
                      DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        initDialog(context,title,msg,null,positiveListener,null,negativeListener);
    }
    private void initDialog(Context context, String title, String msg, String positiveName, DialogInterface.OnClickListener positiveListener, String negativeName, DialogInterface.OnClickListener negativeListener) {
        alertDialog = new AlertDialog.Builder(context, R.style.AlertDialog_Succ);
        if(title!=null){
            alertDialog.setTitle(title);
        }
        if(msg!=null){
            alertDialog.setMessage(msg);
        }
        if(positiveListener!=null){
            alertDialog.setPositiveButton(positiveName==null?"确定":positiveName,positiveListener);
        }
        if(negativeListener!=null){
            alertDialog.setNegativeButton(negativeName==null?"取消":negativeName,negativeListener);
        }
        alertDialog.show();
    }

}
