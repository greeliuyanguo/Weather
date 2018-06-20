package cn.com.gree.weather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author:liuyanguo
 * Date:2018/6/20
 * Time:19:03
 * Description:
 */
public class Weather {

    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
