package dingshi.com.hibook.present;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import dingshi.com.hibook.action.IBookListView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.BookList;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/15 17:45
 */

public class BookSearchPresent extends BasePresent<IBookListView, BaseActivity> {
    public BookSearchPresent(IBookListView view, BaseActivity activity) {
        super(view, activity);
    }

    public void search(HashMap<String, String> map, String page) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<BookList>("BookList") {
            @Override
            protected void onStart(Disposable d) {
                getView().onLoad();
            }

            @Override
            protected void onError(ApiException e) {
                getView().onEmpty();
            }

            @Override
            protected void onSuccess(BookList response) {
                if (response.getError_code() == -1 && response.getJsonData() != null) {
                    getView().onSearch(response);
                } else if (response.getError_code() == 0 && response.getJsonData() != null) {
                    getView().onSuccess(response);
                } else {
                    getView().onEmpty();
                }
            }
        };
        map.put("page", page);
        map = AppSign.buildMap(map);
        Observable<BookList> observable = NetUtils.getGsonRetrofit().search(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
}
