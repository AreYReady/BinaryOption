package com.xkj.binaryoption.diffcallback;

import android.support.v7.util.DiffUtil;

import com.xkj.binaryoption.bean.BeanSymbolTag;

import java.util.List;

/**
 * Created by huangsc on 2017-04-19.
 * TODO:
 */

public class SymbolTagDiff extends DiffUtil.Callback {
    private List<BeanSymbolTag> mOldDatas, mNewDatas;//看名字

    public SymbolTagDiff(List<BeanSymbolTag> mOldDatas, List<BeanSymbolTag> mNewDatas) {
        this.mOldDatas = mOldDatas;
        this.mNewDatas = mNewDatas;
    }

    //老数据集size
    @Override
    public int getOldListSize() {
        return mOldDatas != null ? mOldDatas.size() : 0;
    }

    //新数据集size
    @Override
    public int getNewListSize() {
        return mNewDatas != null ? mNewDatas.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldDatas.get(oldItemPosition).getSymbol().equals(mNewDatas.get(newItemPosition).getSymbol());
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BeanSymbolTag beanOld = mOldDatas.get(oldItemPosition);
        BeanSymbolTag beanNew = mNewDatas.get(newItemPosition);
        if(!beanOld.getAmount().equals(beanNew.getAmount())){
            return false;
        }
        return true; //默认两个data内容是相同的
    }

//    @Nullable
//        @Override
//        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
//            BeanSymbolTag beanOld = mOldDatas.get(oldItemPosition);
//            BeanSymbolTag beanNew = mNewDatas.get(newItemPosition);
//            Bundle payload = new Bundle();
//            if(!beanOld.getProfit().equals(beanNew.getProfit())){
//                payload.putString(PROFIT,PROFIT);
//            }
//            if(!beanNew.getOpenPrice().equals(beanNew.getOpenPrice())) {
//                payload.putString(CURRENT_PRICE, CURRENT_PRICE);
//            }
//            if (payload.size() == 0)//如果没有变化 就传空
//                return null;
//            return payload;
//    }
}
