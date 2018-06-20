package cn.com.gree.weather.util;

import android.content.Context;
import android.content.SharedPreferences;

import cn.com.gree.weather.base.WeatherApplication;

/**
 * Author:liuyanguo
 * Date:2017/12/15
 * Time:08:57
 * Description:本地信息配置工具类
 */

public class LocalConfigSPUtil {
    private static final String TAG = "LocalConfigSPUtil";
    private static LocalConfigSPUtil mInstance = null;
    private SharedPreferences mShared;

    public static final String FILE_NAME = "config";
    public static final int MODE = Context.MODE_PRIVATE;

    public static LocalConfigSPUtil getInstance() {
        if (null == mInstance) {
            synchronized (LocalConfigSPUtil.class) {
                if (null == mInstance) {
                    mInstance = new LocalConfigSPUtil(WeatherApplication.getContext());
                }
            }
        }
        return mInstance;
    }

    private LocalConfigSPUtil(Context context) {
        mShared = context.getSharedPreferences(FILE_NAME, MODE);
    }

    public void addStringData(String name, String value) {
        SharedPreferences.Editor editor = mShared.edit();
        editor.putString(name, value);
        editor.apply();
    }

    /**
     * editor.apply()和editor.commit()方法异同：
     * 1.apply没有返回值而commit返回boolean表明修改是否成功
     * 2.apply是将修改数据原子提交到内存，而后异步真正提交到硬件磁盘，而commit是同步的提交到硬件磁盘，因此
     * 在多个并发的提交commit的时候，他们会等待正在处理的commit保存到磁盘后在操作，从而降低了效率。而apply
     * 只是原子的提交到内容，后面有调用apply的函数的将会直接覆盖前面的内存数据，这样从一定程度上提高了很多效率
     * 3.apply方法不会提示任何失败的提示
     * 总结：
     * 由于在一个进程中，SharedPeference是单实例，一般不会出现并发冲突，如果对提交的结果不关心的话，建议使用apply，
     * 当然需要确保提交成功切有后续操作的话，还是需要用commit的
     */

    public String getStringData(String name) {
        return mShared.getString(name, "");
    }

    public String getStringData(String name, String defaultvalue) {
        return mShared.getString(name, defaultvalue);
    }

    public void addIntData(String name, int value) {
        SharedPreferences.Editor editor = mShared.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public int getIntData(String name) {
        return mShared.getInt(name, -1);
    }

    public int getIntData(String name, int defaultvalue) {
        return mShared.getInt(name, defaultvalue);
    }

    public void addLongData(String name, long value) {
        SharedPreferences.Editor editor = mShared.edit();
        editor.putLong(name, value);
        editor.apply();
    }

    public long getLongData(String name) {
        return mShared.getLong(name, 0);
    }

    public void addBooleanData(String name, boolean value) {
        SharedPreferences.Editor editor = mShared.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public boolean getBooleanData(String name) {
        return mShared.getBoolean(name, false);
    }

    public boolean getBooleanData(String name, boolean defaultvalue) {
        return mShared.getBoolean(name, defaultvalue);
    }
}
