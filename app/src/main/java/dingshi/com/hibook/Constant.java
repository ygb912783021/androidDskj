package dingshi.com.hibook;

import com.hyphenate.easeui.EaseConstant;

import dingshi.com.hibook.utils.AppSign;

/**
 * @author wangqi
 * @since 2017/11/8 10:24
 */

public class Constant extends EaseConstant {


    public static final String BUGLY_RELEASE_APPID = "94d825f05c";
    public static final String BUGLY_DEBUG_APPID = "ca322bafdf";

    public static final String ZFB_APPID = "2017101209259403";
    public static final String WEIXIN_APP_ID = "wx642fd79d7b199d92";
    public static final String WEIXIN_SECRET = "9c1a6f1794caa029a3f13b311f64eb1d";


    /**
     * 分享卡邀请
     *
     * @param userId
     * @param cardId
     * @return
     */
    public static String getShareCardUrl(String userId, String cardId) {
        return "http://m.linkbooker.com/qrcode?user_id=" + userId + "&card_id=" + cardId + "&" + AppSign.buidParam(AppSign.getParams());
    }

    /**
     * 图书馆
     *
     * @param userId
     * @param catalogId
     * @param type      类型1.图书馆，2.书友会
     * @param bookNum   藏书数量
     * @param sbookNum  共享图书数量
     * @return
     */
    public static String getCatalogUrl(String userId, String catalogId, int type, int bookNum, int sbookNum, int userTotal) {
        return "http://m.linkbooker.com/users/signin?catalog="
                + catalogId + "&user_id=" + userId + "&type=" + type + "&book_num=" + bookNum + "&sbook_num=" + sbookNum + "&pnum=" + userTotal;
    }

    /**
     * 卡片交换
     *
     * @param userId
     * @param invoteCode
     * @return
     */
    public static String getInviteUrl(String userId, String invoteCode) {
        return "http://m.linkbooker.com/users/register?user_id=" + userId + "&invite_code=" + invoteCode;
    }

    public static String UPLOAD_URL = "http://m.linkbooker.com/download";
    public static  boolean isEditDelete=false;


}
