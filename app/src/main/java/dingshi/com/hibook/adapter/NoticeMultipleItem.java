package dingshi.com.hibook.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author wangqi
 * @since 2018/1/29 10:14
 */

public class NoticeMultipleItem<T> implements MultiItemEntity {


    /**
     * 通知消息
     */
    public static final int NOTICE_MSG = 0x01;
    /**
     * 通知文本
     */
    public static final int NOTICE_TXT = 0x02;

    private int itemType;


    public NoticeMultipleItem(int itemType, T t) {
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
