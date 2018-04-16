package dingshi.com.hibook.action;

import dingshi.com.hibook.bean.Case2Book;

/**
 * @author wangqi
 * @since 2017/12/15 15:32
 */

public interface ICase2BookView {
    void onSuccess(Case2Book bookList);

    void onError();
}
