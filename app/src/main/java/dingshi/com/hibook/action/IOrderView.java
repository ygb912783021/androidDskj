package dingshi.com.hibook.action;

import dingshi.com.hibook.bean.Order;

/**
 * @author wangqi
 * @since 2017/11/14 11:19
 */

public interface IOrderView {
    void refresh(Order order);

    void loadMore(Order order);
}
