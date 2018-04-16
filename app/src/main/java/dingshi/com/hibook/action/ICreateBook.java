package dingshi.com.hibook.action;

import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.Douban;
import dingshi.com.hibook.bean.Zxing;

/**
 * @author wangqi
 * @since 2017/12/18 13:27
 */

public interface ICreateBook extends IRequestView {
    void getBook(BookDetails bookDetails);

    void getDouban(Douban douban);

}
