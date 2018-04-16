package dingshi.com.hibook.action;

import android.graphics.Bitmap;

import dingshi.com.hibook.bean.card.CardDetails;

/**
 * @author wangqi
 * @since 2017/12/20 11:24
 */

public interface ICardDetails {
    /**
     * 名片详情
     */
    void onCardDetails(CardDetails cardDetails);

    /**
     * 设置主名片
     */
    void onMainCard();

    /**
     * 错误信息
     */
    void onError(String error);

    /**
     * 生成的二维码
     *
     * @param bitmap
     */
    void onLoadZxing(Bitmap bitmap);

    /**
     * 交换名片
     *
     * @param cardDetails
     */
    void onSweep(CardDetails cardDetails);

}
