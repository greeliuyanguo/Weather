package cn.com.gree.weather.ui;

import android.content.Intent;
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

        initConfig();
    }

    /**
     * 初始化配置基本逻辑
     */
    private void initConfig() {
        if (null != LocalConfigSPUtil.getInstance().getStringData("weather", null)) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public String getToolbarTitle() {
        return null;
    }

    @Override
    public void activityCloseExitTransition() {
        LogUtil.d(TAG, "该页面退出不需要动画效果...");
    }
}
