package dingshi.com.hibook.test;

public class Test {
    @org.junit.Test
    public void main() {

//        Strategy strategy = new Strategy();
//
//        strategy.setStrategy(new CatalogStrategy());

//        String result="dasdasdads";
//        String result="http://www.baidu.com";
//        String result = "http://testwap.linkbooker.com?sn=su120010";
//        String result="http://testm.linkbooker.com/qrcode?user_id=123123123&card_id=u6789&timestamp=&os_type=&os_version=&channel=";
//        String result="http://m.linkbooker.com/users/signin?catalog=15005024&user_id=2&type=1&book_num=1&sbook_num=1";

          String result="http://m.linkbooker.com/users/signin?catalog=27&user_id=526692416898469894&type=1&book_num=0&sbook_num=0";
//        System.out.print(result);

        StrategyHelper helper=new StrategyHelper();
        helper.strage(result);

    }
}
//  if (result.startsWith("http://")) {
//        String[] split = result.split("\\?");
//
//        if (split.length > 1) {
//            String[] values = split[1].split("&");
//            for (int i = 0; i < values.length; i++) {
//                if (values[i].contains("sn=")) {
//                    bookCaseId = values[i].substring(values[i].indexOf("=") + 1, values[i].length());
//                }
//                if (values[i].contains("user_id=")) {
//                    userId = values[i].substring(values[i].indexOf("=") + 1, values[i].length());
//                }
//                if (values[i].contains("card_id=")) {
//                    cardId = values[i].substring(values[i].indexOf("=") + 1, values[i].length());
//                }
//            }
//        }
//
//        if (!TextUtils.isEmpty(bookCaseId)) {
//            jumpBookCase(bookCaseId);
//        } else if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(cardId)) {
//            sweepCard(userId, cardId);
//        } else {
//            Intent intent = new Intent(this, WebActivity.class);
//            intent.putExtra("url", result);
//            startActivity(intent);
//            finish();
//        }

