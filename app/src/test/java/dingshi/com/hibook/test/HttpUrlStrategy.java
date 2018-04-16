package dingshi.com.hibook.test;

/**
 * @author wangqi
 * @since 2018/3/2 13:36
 */

public class HttpUrlStrategy implements UrlStrategy {
    @Override
    public String pattern(String url) {
        if (url.startsWith("http://")) {
            return url;
        }
        return "";
    }
}
