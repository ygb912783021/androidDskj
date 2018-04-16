package dingshi.com.hibook.share;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

/**
 * @author wangqi
 * @since 2017/11/15 18:14
 */

public interface IWeixin {
    int RESULT_SUCCESS = 0x01;
    int RESULT_CANCELD = 0x02;
    int RESULT_FAIL = 0x03;


    /**
     * 微信好友
     */
    int WXSceneSession = SendMessageToWX.Req.WXSceneSession;
    /**
     * 喜欢的
     */
    int WXSceneFavorite = SendMessageToWX.Req.WXSceneFavorite;

    /***
     * 朋友圈
     */
    int WXSceneTimeline = SendMessageToWX.Req.WXSceneTimeline;

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
     * @param payWeixin
     * @param callback
     */
    void pay(PayWeixin payWeixin, ShareCallBack callback);
}
