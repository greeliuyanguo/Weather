package cn.com.gree.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Author:liuyanguo
 * Date:2018/6/20
 * Time:18:50
 * Description:
 */
public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
