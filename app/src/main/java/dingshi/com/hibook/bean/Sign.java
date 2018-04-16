package dingshi.com.hibook.bean;

/**
 * @author wangqi
 * @since 2017/11/15 13:45
 */

public class Sign extends Result {


    /**
     * jsonData : {"sign":"apiname=com.alipay.account.auth&app_id=2017101209259403&app_name=你好读&auth_type=AUTHACCOUNT&biz_type=openservice&pid=2088821323872701&product_id=APP_FAST_LOGIN&scope=kuaijie&sign_type=RSA2&target_id=201711152017101209259403&sign=T1WOH%2BL%2Fls8E1WwxKzow7zEU9S6iwfRc8nTJ6mcPK2K9NNsZZsyjZpyJqXrgsdn9CWijo73YltNqykTQoLeM0LCL2dpXwusDxhkGkDK14O8myKxrXsUggB6qK%2BgqoaH5x2Xo2lC0SzmVrT4gxiSx30pCOhUnZRGJw%2BpCkdPZttddgyPRMCL7SkXRffLhvvS1VVu1H2WfyW25vdttFOG3k4PkCFxERwnjqAtWAqH8YASecuMq7otyWyc%2Fj%2FMk5ohebAUJyMSQLaVxmoQm7NfBOpFf8aLhj1kbaEzM5nyHAEyss1G5YemJlsi28f4M0gFnHW0EFk2CJsPiXTwDq3gIgQ%3D%3D"}
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
         * sign : apiname=com.alipay.account.auth&app_id=2017101209259403&app_name=你好读&auth_type=AUTHACCOUNT&biz_type=openservice&pid=2088821323872701&product_id=APP_FAST_LOGIN&scope=kuaijie&sign_type=RSA2&target_id=201711152017101209259403&sign=T1WOH%2BL%2Fls8E1WwxKzow7zEU9S6iwfRc8nTJ6mcPK2K9NNsZZsyjZpyJqXrgsdn9CWijo73YltNqykTQoLeM0LCL2dpXwusDxhkGkDK14O8myKxrXsUggB6qK%2BgqoaH5x2Xo2lC0SzmVrT4gxiSx30pCOhUnZRGJw%2BpCkdPZttddgyPRMCL7SkXRffLhvvS1VVu1H2WfyW25vdttFOG3k4PkCFxERwnjqAtWAqH8YASecuMq7otyWyc%2Fj%2FMk5ohebAUJyMSQLaVxmoQm7NfBOpFf8aLhj1kbaEzM5nyHAEyss1G5YemJlsi28f4M0gFnHW0EFk2CJsPiXTwDq3gIgQ%3D%3D
         */

        private String sign;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
