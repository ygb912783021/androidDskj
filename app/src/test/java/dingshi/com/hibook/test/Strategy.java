package dingshi.com.hibook.test;

/**
 * @author wangqi
 * @since 2018/3/2 13:29
 */

public class Strategy {
    UrlStrategy strategy;

    public void setStrategy(UrlStrategy strategy) {
        this.strategy = strategy;
    }

    public String pattern(String url) {
        return strategy.pattern(url);
    }
}
