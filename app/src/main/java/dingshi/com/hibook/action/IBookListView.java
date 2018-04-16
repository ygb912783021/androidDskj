package dingshi.com.hibook.action;

import dingshi.com.hibook.bean.BookList;
import dingshi.com.hibook.bean.Result;

/**
 * @author wangqi
 * @since 2017/12/15 17:46
 */

public interface IBookListView extends IRequestView<BookList>{
    void onSearch(BookList bookList);
}
