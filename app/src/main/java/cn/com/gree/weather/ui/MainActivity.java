package cn.com.gree.weather.ui;

import android.os.Bundle;

import cn.com.gree.weather.R;
import cn.com.gree.weather.base.BaseActivity;
import cn.com.gree.weather.util.LogUtil;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public String getToolbarTitle() {
        return "天气";
    }

    @Override
    public void activityCloseExitTransition() {
        LogUtil.d(TAG, "该页面退出不需要动画效果...");
    }
}
