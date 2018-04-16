package dingshi.com.hibook.action;

import dingshi.com.hibook.bean.BookCase;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.BookPerson;
import dingshi.com.hibook.bean.BookTalent;
import dingshi.com.hibook.bean.CommentGrade;
import dingshi.com.hibook.bean.CommentInfo;
import dingshi.com.hibook.bean.Result;

/**
 * @author wangqi
 * @since 2017/12/12 15:09
 */

public interface IBookDetailsView extends IRequestView<Result> {
    void onBookDetails(BookDetails bookDetails);

    void onBookCase(BookCase bookCase);

    void onBookTalent(BookTalent bookTalent);

    void onBookEval(CommentGrade commentGrade);

    void onBookFriend(CommentInfo commentInfo);

    void onBookPerson(BookPerson bookPerson);

    void onBookPraise(String type);


}
