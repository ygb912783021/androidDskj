package dingshi.com.hibook.utils.strategy;

/**
 * @author wangqi
 * @since 2018/3/2 13:57
 */

public class StrategyHelper {

    public void strage(String url) {
        Strategy strategy = new Strategy();

        strategy.setStrategy(new HttpUrlStrategy());
        String http = strategy.pattern(url);

        if ("".equals(http)) {
            System.out.print("不是http");
        } else {
            strategy.setStrategy(new CardStrategy());
            String card = strategy.pattern(url);
            if (!"".equals(card)) {
                System.out.println("card");
                System.out.println(card);
                return;
            }
            strategy.setStrategy(new CaseStrategy());
            String bookCase = strategy.pattern(url);

            if (!"".equals(bookCase)) {
                System.out.println("bookCase");
                System.out.println(bookCase);
                return;
            }

            strategy.setStrategy(new CatalogStrategy());
            String catalog = strategy.pattern(url);

            if (!"".equals(catalog)) {
                System.out.println("catalog");
                System.out.println(catalog);
                return;
            }

            System.out.println("http");
            System.out.println(http);
        }
    }
}
