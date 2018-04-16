package dingshi.com.hibook.test;

public class CardStrategy implements UrlStrategy {
    @Override
    public String pattern(String url) {
        String[] split = url.split("\\?");
        String userId = "";
        String cardId = "";
        if (split.length > 1) {
            String[] values = split[1].split("&");
            for (int i = 0; i < values.length; i++) {
                if (values[i].contains("user_id=")) {
                    userId = values[i].substring(values[i].indexOf("=") + 1, values[i].length());
                }
                if (values[i].contains("card_id=")) {
                    cardId = values[i].substring(values[i].indexOf("=") + 1, values[i].length());
                }
            }
        }
        if (!"".equals(userId) && !"".equals(cardId)) {
            return userId + "=" + cardId;
        }
        return "";
    }
}
