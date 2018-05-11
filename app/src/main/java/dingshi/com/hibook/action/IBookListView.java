package dingshi.com.hibook.action;

import dingshi.com.hibook.bean.BookList;

/**
 * @author wangqi
 * @since 2017/12/15 17:46
 */

public interface IBookListView extends IRequestView<BookList>{
    void onSearch(BookList bookList);
}
