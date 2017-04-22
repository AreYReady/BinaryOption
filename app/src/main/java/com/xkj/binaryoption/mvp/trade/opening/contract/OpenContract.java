package com.xkj.binaryoption.mvp.trade.opening.contract;

import com.xkj.binaryoption.bean.RealTimeDataList;

/**
 * Created by huangsc on 2017-04-19.
 * TODO:
 */

public class OpenContract {
    
public interface View{
   void receRealTimeData(RealTimeDataList realTimeDataList);
}

public interface Presenter{
    void sendSubSymbol(String symbol);
    void onDestroy();

}

public interface Model{
    void sendSubSymbol(String symbols);
}


}