package cn.com.gree.weather.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import cn.com.gree.weather.R;
import cn.com.gree.weather.base.BaseActivity;
import cn.com.gree.weather.gson.Forecast;
import cn.com.gree.weather.gson.Weather;
import cn.com.gree.weather.util.HttpUtil;
import cn.com.gree.weather.util.LocalConfigSPUtil;
import cn.com.gree.weather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity {
    private static final String TAG = WeatherActivity.class.getSimpleName();

    private ScrollView mSvWeatherLayout;
    private TextView mTvTitleCity, mTvTitleUpdateTime, mTvDegreeText, mTvWeatherInfoText;
    private TextView mTvAqiText, mTvPm25Text, mTvComfortText, mTvCarWashText, mTvSportText;
    private LinearLayout mLlForecastLayout;

    private static final String APP_KEY = "4bf7b7f80b0647ac9fd83d3f06e58dc2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initViews();
        initData();
    }

    /**
     * 初始化各种控件
     */
    private void initViews() {
        mSvWeatherLayout = findViewById(R.id.weather_layout);
        mTvTitleCity = findViewById(R.id.title_city);
        mTvTitleUpdateTime = findViewById(R.id.title_update_time);
        mTvDegreeText = findViewById(R.id.degree_text);
        mTvWeatherInfoText = findViewById(R.id.weather_info_text);
        mTvAqiText = findViewById(R.id.aqi_text);
        mTvPm25Text = findViewById(R.id.pm25_text);
        mTvComfortText = findViewById(R.id.comfort_text);
        mTvCarWashText = findViewById(R.id.car_wash_text);
        mTvSportText = findViewById(R.id.sport_text);
        mLlForecastLayout = findViewById(R.id.forecast_layout);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String weatherString = LocalConfigSPUtil.getInstance().getStringData("weather", null);
        if (!TextUtils.isEmpty(weatherString)) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            //无缓存时去服务器查询天气
            String weatherId = getIntent().getStringExtra("weather_id");
            mSvWeatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    /**
     * 根据天气id请求城市天气信息
     *
     * @param weatherId
     */
    private void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=" + APP_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null != weather && "ok".equals(weather.status)) {
                            LocalConfigSPUtil.getInstance().addStringData("weather", responseText);
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 处理并显示Weather实体类中的数据
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        mTvTitleCity.setText(cityName);
        mTvTitleUpdateTime.setText(updateTime);
        mTvDegreeText.setText(degree);
        mTvWeatherInfoText.setText(weatherInfo);
        mLlForecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, mLlForecastLayout, false);
            TextView dataText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dataText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            mLlForecastLayout.addView(view);
        }
        if (null != weather.aqi) {
            mTvAqiText.setText(weather.aqi.city.aqi);
            mTvPm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        mTvComfortText.setText(comfort);
        mTvCarWashText.setText(carWash);
        mTvSportText.setText(sport);
        mSvWeatherLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public String getToolbarTitle() {
        return null;
    }
}
