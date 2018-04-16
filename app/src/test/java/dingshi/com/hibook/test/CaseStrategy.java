package dingshi.com.hibook.test;

import org.junit.*;

/**
 * @author wangqi
 * @since 2018/3/2 13:38
 */

public class CaseStrategy implements UrlStrategy {

    //    String result = "http://testwap.linkbooker.com?sn=su120010";
    @Override
    public String pattern(String url) {
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
