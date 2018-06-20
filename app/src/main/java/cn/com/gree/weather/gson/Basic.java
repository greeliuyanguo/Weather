package cn.com.gree.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Author:liuyanguo
 * Date:2018/6/20
 * Time:18:45
 * Description:
 */
public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
