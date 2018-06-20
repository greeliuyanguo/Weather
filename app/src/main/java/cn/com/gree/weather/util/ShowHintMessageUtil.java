package cn.com.gree.weather.util;

import android.widget.Toast;

import cn.com.gree.weather.base.WeatherApplication;

/**
 * Author:liuyanguo
 * Date:2017/12/9
 * Time:15:05
 * Description:信息提示类
 */

public class ShowHintMessageUtil {
    private static final String TAG = "ShowHintMessageUtil";

    public static void showToast(String message) {
        Toast.makeText(WeatherApplication.getContext(), message, Toast.LENGTH_SHORT).show();
        LogUtil.d(TAG, message);
    }
}
