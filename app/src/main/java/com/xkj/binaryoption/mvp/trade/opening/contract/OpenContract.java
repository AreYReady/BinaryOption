package com.xkj.binaryoption.mvp.trade.opening.contract;

import com.xkj.binaryoption.bean.BeanHistoryRequest;
import com.xkj.binaryoption.bean.RealTimeDataList;

import java.util.List;
import java.util.Map;

/**
 * Created by huangsc on 2017-04-19.
 * TODO:
 */

public class OpenContract {
    
public interface View{
   void eventRealTimeData(RealTimeDataList realTimeDataList);

    void eventAllSymbolsData(Map<String,Integer> mAllSymbolsDigits);
}

public interface Presenter{
    void sendSubSymbol(String symbol);
    void onDestroy();

    /**
     * 设置当前symbol
     * @param currentSymbol
     */
    void setCurrentSymbol(String currentSymbol);
    void sendHistoryPrices(BeanHistoryRequest beanHistoryRequest);


}

public interface Model{
    void sendSubSymbol(String symbols);
}


}