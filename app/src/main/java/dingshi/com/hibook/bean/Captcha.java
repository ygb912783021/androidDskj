package dingshi.com.hibook.bean;

/**
 * @author wangqi
 * @since 2017/11/9 10:17
 */

public class Captcha extends Result {

    /**
     * jsonData : {"captcha":274142}
     * error_code : 0
     * error_msg :
     */

    private JsonDataBean jsonData;


    public JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }


    public static class JsonDataBean {
        /**
         * captcha : 274142
         */

        private int captcha;

        public int getCaptcha() {
            return captcha;
        }

        public void setCaptcha(int captcha) {
            this.captcha = captcha;
        }
    }


}
