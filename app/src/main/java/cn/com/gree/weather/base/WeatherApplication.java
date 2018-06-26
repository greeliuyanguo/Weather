package cn.com.gree.weather.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

import java.util.List;

/**
 * Author:liuyanguo
 * Date:2018/6/19
 * Time:10:53
 * Description:
 */
public class WeatherApplication extends Application {

    public static final String APP_KEY = "4bf7b7f80b0647ac9fd83d3f06e58dc2";
    private static final String TAG = WeatherApplication.class.getSimpleName();
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        initContext();
        initLitePal();
    }

    private void initLitePal() {
        LitePal.initialize(sContext);
    }

    private void initContext() {
        sContext = getApplicationContext();
    }

    /**
     * 获取应用级别的Context对象
     *
     * @return
     */
    public static Context getContext() {
        if (null != sContext) {
            return sContext;
        } else {
            return null;
        }
    }

    /**
     * 判断应用程序是否运行在前台
     *
     * @return
     */
    public static boolean isAppRunningOnForeground() {
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        String currentAppProcessName = getContext().getApplicationInfo().processName;
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (null == appProcesses) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcesses) {
            //一旦进程列表里面有本APP的包名(该应用在进程里面的名字)并且该进程的重要级别为前台进程，则APP运行在前台
            if (appProcessInfo.processName.equals(currentAppProcessName) &&
                    appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
