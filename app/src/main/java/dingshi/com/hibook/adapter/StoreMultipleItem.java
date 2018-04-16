package dingshi.com.hibook.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author wangqi
 *         Created by apple on 2017/10/26.
 */

public class StoreMultipleItem<T> implements MultiItemEntity {
    /**
     * 轮播
     */
    public static final int BOOK_BANNER = 1;
    /**
     * 新闻
     */
    public static final int BOOK_NEWS = 2;


    /**
     * 附近书柜
     */
    public static final int BOOK_CASE = 3;
    /**
     * 书房达人
     */
    public static final int BOOK_TALENT = 4;
    /**
     * 最受关注的图书
     */
    public static final int BOOK_CENTRE = 5;
    /**
     * 畅销图书榜
     */
    public static final int BOOK_SELLING = 6;
    /**
     * 你可能感兴趣
     */
    public static final int BOOK_TASTE = 7;
    /**
     * 广告
     */
    public static final int BOOK_ADVERTISE = 8;
    /**
     * 书友会
     */
    public static final int BOOK_RALLY = 9;

    private int itemType;


    public StoreMultipleItem(int itemType, T t) {
        this.itemType = itemType;
        this.t = t;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    T t;

    public void setData(T t) {
        this.t = t;
    }


    public T getData() {
        return t;
    }
}
