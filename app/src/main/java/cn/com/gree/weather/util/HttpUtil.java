package cn.com.gree.weather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Author:liuyanguo
 * Date:2018/6/20
 * Time:9:05
 * Description:
 */
public class HttpUtil {

    /**
     * 异步回调处理
     *
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
