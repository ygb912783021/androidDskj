package dingshi.com.hibook.share;

/**
 * @author wangqi
 * @since 2017/11/15 18:16
 */

public interface ShareCallBack {
    void onSuccess(String result);

    void onCanceled(String result);

    void onFailed(String result);
}
