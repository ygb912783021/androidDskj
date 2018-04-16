package dingshi.com.hibook.utils.strategy;

import android.util.Log;

public class CatalogStrategy implements UrlStrategy {
    @Override
    public String pattern(String url) {

        Log.i("UrlStrategy", "CatalogStrategy");

        String[] split = url.split("\\?");

        String catalog = "";
        String type = "";

        if (split.length > 1) {
            String[] values = split[1].split("&");
            for (int i = 0; i < values.length; i++) {
                if (values[i].contains("catalog=")) {
                    catalog = values[i].substring(values[i].indexOf("=") + 1, values[i].length());
                }
                if (values[i].contains("type=")) {
                    type = values[i].substring(values[i].indexOf("=") + 1, values[i].length());
                }

            }
        }

        if (!"".equals(catalog) && !"".equals(type)) {
            return catalog + "=" + type;
        }
        return "";
    }
}
