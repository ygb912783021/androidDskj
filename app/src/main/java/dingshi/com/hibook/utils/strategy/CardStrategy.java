package dingshi.com.hibook.utils.strategy;

import android.util.Log;

public class CardStrategy implements UrlStrategy {
    @Override
    public String pattern(String url) {

        Log.i("UrlStrategy","CardStrategy");

        String[] split = url.split("\\?");
        if (split.length > 1) {
            String[] values = split[1].split("&");
            for (int i = 0; i < values.length; i++) {
                if (values[i].contains("card_id=")) {
                    return values[i].substring(values[i].indexOf("=") + 1, values[i].length());
                }
            }
        }
        return "";
    }
}
