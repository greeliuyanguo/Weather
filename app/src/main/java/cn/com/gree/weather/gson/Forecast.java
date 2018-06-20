package cn.com.gree.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Author:liuyanguo
 * Date:2018/6/20
 * Time:18:59
 * Description:
 */
public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature {

        public String max;

        public String min;
    }

    public class More {

        @SerializedName("txt_d")
        public String info;
    }
}
