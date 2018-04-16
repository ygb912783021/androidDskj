package dingshi.com.hibook.utils.strategy;

import android.util.Log;

/**
 * @author wangqi
 * @since 2018/3/2 13:38
 */

public class CaseStrategy implements UrlStrategy {

    @Override
    public String pattern(String url) {
        Log.i("UrlStrategy","CaseStrategy");

        String[] split = url.split("\\?");
        if (split.length > 1) {
            String[] values = split[1].split("&");
            for (int i = 0; i < values.length; i++) {
                if (values[i].contains("sn=")) {
                    return values[i].substring(values[i].indexOf("=") + 1, values[i].length());
                }
            }
        }
        return "";
    }
}
