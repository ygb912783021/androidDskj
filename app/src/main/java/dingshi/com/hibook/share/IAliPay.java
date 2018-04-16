package dingshi.com.hibook.share;

/**
 * @author wangqi
 * @since 2017/11/18 18:21
 */

public interface IAliPay {
    int RESULT_SUCCESS = 0x01;
    int RESULT_CANCELD = 0x02;
    int RESULT_FAIL = 0x03;


    /**
     * 分享
     *
     * @param params
     * @param callback
     */
    void share(ShareParams params, ShareCallBack callback);

    /**
     * 登录
     *
     * @param callback
     */
    void login(ShareCallBack callback);


    /**
     * 支付
     *
     * @param orderInfo
     * @param callback
     */
    void pay(String orderInfo, ShareCallBack callback);
}
