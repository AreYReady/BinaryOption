package com.xkj.binaryoption.mvp.trade.opening.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.xkj.binaryoption.bean.BeanHistoryRequest;
import com.xkj.binaryoption.bean.BeanOrderRequest;
import com.xkj.binaryoption.bean.EventBusAllSymbol;
import com.xkj.binaryoption.bean.RealTimeDataList;
import com.xkj.binaryoption.constant.MessageType;
import com.xkj.binaryoption.message.MessageHeart;
import com.xkj.binaryoption.mvp.trade.opening.contract.OpenContract;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

/**
* Created by huangsc on 2017/04/19
*/

public class OpenPresenterImpl implements OpenContract.Presenter{
    private OpenContract.View mView;
    private Context mContext;
    private Handler mHandler;
    private Map<String,List<RealTimeDataList.BeanRealTime>> mRealTimeDataMap=new ArrayMap<>();
    private String mCurrentSymbol;
    /**
     * 判断是否需要刷新
     */

    public OpenPresenterImpl(Context context,OpenContract.View mView) {
        mContext=context;
        this.mView=mView;
        EventBus.getDefault().register(this);
    }

    /**
     * 订阅实时数据类型
     * @param symbol
     */
    @Override
    public void sendSubSymbol(String symbol) {
        sendMessageToServer("{\"msg_type\":1010,\"symbol\":\"" + symbol + "\"}");
    }

    @Override
    public void onDestroy() {
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 获取到实时数据
     *
     * @param realTimeDataList
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGetRealTimeData(RealTimeDataList realTimeDataList) {
        Log.i("hsc", "onGetRealTimeData: ");
        mView.eventRealTimeData(realTimeDataList);

    }

    /**
     * 客户端主动发送心跳
     */
    private void sendHeartBeat(){
        if(mHandler!=null){
            sendMessageToServer(MessageType.TYPE_ORDER_SREVER_TIME);
        }
    }
    private void sendMessageToServer(String data) {
        Message message = new Message();
        message.obj = (data);
        mHandler.sendMessage(message);
    }
    @Subscribe(sticky = true)
    public void getHandler(Handler handler){
        mHandler=handler;
    }
    @Subscribe
    public void getHeart(MessageHeart messageHeart){
        sendMessageToServer("{\"msg_type\":2}");
    }
    @Subscribe
    public void eventOrderRequest(BeanOrderRequest beanOrderRequest){
        sendMessageToServer( new Gson().toJson(beanOrderRequest));
    }

    /**
     * 接受所有商品产数
     * @param eventBusAllSymbol
     */
    @Subscribe(sticky = true)
    public void EventAllSymbolsData(EventBusAllSymbol eventBusAllSymbol){
        ArrayMap<String, Integer> map=new ArrayMap<>();
        for(EventBusAllSymbol.ItemSymbol itemSymbol:eventBusAllSymbol.getItems()){
            map.put(itemSymbol.getSymbol(),itemSymbol.getDigits());
        }
      mView.eventAllSymbolsData(map);
  }
    public void setCurrentSymbol(String currentSymbol){
        mCurrentSymbol=currentSymbol;
    }

    @Override
    public void sendHistoryPrices(BeanHistoryRequest beanHistoryRequest) {
        sendMessageToServer(new Gson().toJson(beanHistoryRequest));
    }


}