package cn.com.gree.weather.Base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.gree.weather.R;
import cn.com.gree.weather.util.ShowHintMessageUtil;
import cn.com.gree.weather.widget.StatusBarView;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private boolean isCurrentRunningForeground = true;

    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    /**
     * 外部Activity启动本Activity
     *
     * @param activity
     */
    public static void launch(Activity activity, Class targetClass) {
        Intent intent = new Intent(activity, targetClass);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_right_to_left, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucent();
        initToolbar();
    }

    /**
     * 设置沉浸式状态栏
     */
    private void setTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //获取windowphone下的decorView
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            int count = decorView.getChildCount();
            //判断是否已经添加了statusBarView
            if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
                decorView.getChildAt(count - 1).setBackgroundResource(R.color.statusBarColor);
            } else {
                //新建一个和状态栏高宽的view
                StatusBarView statusView = createStatusBarView(this, 0xff3F51B5, 0);
                decorView.addView(statusView);
            }
            ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            //rootview不会为状态栏留出状态栏空间
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 创建一个状态栏控件
     *
     * @param activity
     * @param color
     * @param alpha
     * @return
     */
    private StatusBarView createStatusBarView(Activity activity, int color, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(activity);
        int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int height = -1;
        if (identifier > 0) {
            height = getResources().getDimensionPixelSize(identifier);
        }
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = mToolbar.findViewById(R.id.title_toolbar);
        mToolbarTitle.setText(getToolbarTitle());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * 每个Activity不同的Activity
     *
     * @return
     */
    public abstract String getToolbarTitle();

    /**
     * 设置标题是否隐藏
     *
     * @param visible
     */
    public void setToolbarTitleVisibility(int visible) {
        mToolbarTitle.setVisibility(visible);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isCurrentRunningForeground) {
            ShowHintMessageUtil.showToast("应用已被切回前台");
            onBecameForeground();
        }
    }

    /**
     * 应用被切回前台
     * 所有需要在应用切回前台的Activity
     * 可以重写该类进行逻辑操作
     */
    protected void onBecameForeground() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isCurrentRunningForeground = WeatherApplication.isAppRunningOnForeground();
        if (!isCurrentRunningForeground) {
            ShowHintMessageUtil.showToast("应用被切换到后台");
            onBecameBackground();
        }
    }

    /**
     * 应用被切换到了后台
     * =所有需要在应用切换到后台的Activity
     * 可以重写该类进行逻辑操作
     */
    protected void onBecameBackground() {
    }

    @Override
    public void finish() {
        super.finish();
        activityCloseExitTransition();
    }

    /**
     * 退场动画
     */
    public void activityCloseExitTransition() {
        overridePendingTransition(0, R.anim.activity_left_to_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
