package cn.com.gree.weather.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Author:liuyanguo
 * Date:2017/11/24
 * Time:9:57
 * Description:避免代码繁多冗余，实现了Closeable这个接口的类都可以用这个工具类
 */

public class CloseUtil {

    public static void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
