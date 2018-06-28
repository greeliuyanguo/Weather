package cn.com.gree.weather.ui;

import android.os.Bundle;

import cn.com.gree.weather.R;
import cn.com.gree.weather.base.BaseActivity;
import cn.com.gree.weather.util.LocalConfigSPUtil;
import cn.com.gree.weather.util.LogUtil;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initConfig();
    }

    @Override
    public int getDefaultStatusBarColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    /**
     * 初始化配置基本逻辑
     */
    private void initConfig() {
        if (null != LocalConfigSPUtil.getInstance().getStringData("weather", null)) {
            launch(this, WeatherActivity.class);
            finish();
        }
    }

    @Override
    public String getToolbarTitle() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
