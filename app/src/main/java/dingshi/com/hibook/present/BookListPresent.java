package dingshi.com.hibook.present;

import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.HashMap;

import dingshi.com.hibook.action.IBookListView;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.BookList;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.SpUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/25 14:59
 */

public class BookListPresent extends BasePresent<IBookListView, BaseFragment> {
    public BookListPresent(IBookListView view, BaseFragment activity) {
        super(view, activity);
    }

    /**
     * 我的图书馆图书列表
     *
     * @param catalog_id
     * @param page
     */
    public void myLibBookList(String catalog_id, String page) {


        HttpRxObserver httpRxObserver = new HttpRxObserver<BookList>("libBookList") {
            @Override
            protected void onStart(Disposable d) {
                getView().onLoad();
            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(BookList bookList) {
                if (bookList.getJsonData() != null) {
                    getView().onSearch(bookList);
                } else {
                    getView().onEmpty();
                }
            }
        };

        HashMap<String, String> map = new HashMap<>();
        map.put("catalog_id", catalog_id);
        map.put("page", page);

        map = AppSign.buildMap(map);

        Observable<BookList> user = NetUtils.getGsonRetrofit().libBookList(map);

        HttpRxObservable.getObservable(user, getActivity(), FragmentEvent.PAUSE).subscribe(httpRxObserver);


    }
}
