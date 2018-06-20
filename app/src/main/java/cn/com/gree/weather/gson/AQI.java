package cn.com.gree.weather.gson;

/**
 * Author:liuyanguo
 * Date:2018/6/20
 * Time:18:47
 * Description:
 */
public class AQI {

    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
