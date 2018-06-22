package cn.com.gree.weather.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;

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

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void defaultGreaterLollipop() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
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
    public void activityCloseExitTransition() {
        LogUtil.d(TAG, "该页面退出不需要动画效果...");
    }
}
