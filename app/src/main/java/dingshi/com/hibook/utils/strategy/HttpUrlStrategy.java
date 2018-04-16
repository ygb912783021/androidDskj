package dingshi.com.hibook.utils.strategy;

import android.util.Log;

/**
 * @author wangqi
 * @since 2018/3/2 13:36
 */

public class HttpUrlStrategy implements UrlStrategy {
    @Override
    public String pattern(String url) {

        Log.i("UrlStrategy", "HttpUrlStrategy");


        if (url.startsWith("http://")) {
            return url;
        }
        return "";
    }
}
