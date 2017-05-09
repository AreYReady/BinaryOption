package com.xkj.binaryoption.easypay;

import com.google.gson.Gson;
import com.opentech.cloud.server.component.api.sdk.ApiClient;
import com.opentech.cloud.server.component.api.sdk.ApiClientFactory;
import com.opentech.cloud.server.component.api.sdk.ApiRequestBuilder;
import com.opentech.cloud.server.component.api.sdk.ApiResponse;

import java.util.Map;

/**
 * Created by huangsc on 2017-05-09.
 * TODO:
 */

public class PayUtils {
    private ApiClient apiClient;
        private static PayUtils instance;
        private PayUtils(){
            this.apiClient = ApiClientFactory.newClient("https://gw.efubao.club/native/", "2017011822055618000");
        }
        public static synchronized PayUtils getInstance() {
            if (instance == null) {
                instance = new PayUtils();
            }
            return instance;
        }
    public void queryOrder(){
        // 查询交易 API名称, API版本
        ApiRequestBuilder      builder = ApiRequestBuilder.newInstance("com.opentech.cloud.easypay.trade.query", "0.0.1");

        // 添加参数 商户号
        builder.addParameter("merchantNo", "WVFV66002JNBC");
        // 添加参数 外部订单号
        builder.addParameter("outTradeNo", "H5090917795840d");

        // 同步调用
		/*
		response = this.apiClient.invoke(builder.build());
		// 判断是否成功
		if(response.isSucceed()) {
			// 调用成功 response.getData(Map.class) 返回结果
			System.out.println(JSON.toJSONString(response.getData(Map.class), true));
		} else {
			// 发生错误
			System.out.println("error: " + response.getErrorCode() + "/" + response.getMsg());
		}
		*/

        // 异步调用
        this.apiClient.invoke(builder.build(), new ApiClient.Listener() {

            @Override
            public void onSucceed(ApiResponse response) {
                // 调用成功 response.getData(Map.class) 返回结果
//                System.out.println(JSON.toJSONString(response.getData(Map.class), true));
                System.out.println(new Gson().toJson(response.getData(Map.class)));
            }

            @Override
            public void onFailed(String errorCode, String msg) {
                // 发生错误
                System.out.println("error: " + errorCode + "/" + msg);
            }

            @Override
            public void onTimeout() {
                // 网络超时
                System.out.println("Network timeout");
            }
        });

    }
    }

