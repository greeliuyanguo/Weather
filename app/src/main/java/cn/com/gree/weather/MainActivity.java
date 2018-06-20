package cn.com.gree.weather;

import android.os.Bundle;

import cn.com.gree.weather.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getToolbarTitle() {
        return "天气";
    }
}
