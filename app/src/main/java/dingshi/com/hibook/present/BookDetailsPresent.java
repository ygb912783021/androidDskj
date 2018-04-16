package dingshi.com.hibook.present;

import android.content.Intent;
import android.widget.Toast;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import dingshi.com.hibook.MyApplicationLike;
import dingshi.com.hibook.action.IBookDetailsView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.BookCase;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.BookPerson;
import dingshi.com.hibook.bean.BookTalent;
import dingshi.com.hibook.bean.CommGradeAdd;
import dingshi.com.hibook.bean.CommentGrade;
import dingshi.com.hibook.bean.CommentInfo;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * @author wangqi
 * @since 2017/12/12 15:08
 */

public class BookDetailsPresent extends BasePresent<IBookDetailsView, BaseActivity> {
    public BookDetailsPresent(IBookDetailsView view, BaseActivity activity) {
        super(view, activity);
    }

    /**
     * 加载图书详情
     *
     * @param bookIsbn
     */
    public void loadBookDetails(String bookIsbn) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<BookDetails>("bookShow") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {

            }

            @Override
            protected void onSuccess(BookDetails response) {
                if (response.getJsonData() != null) {
                    getView().onBookDetails(response);
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", bookIsbn);
        map = AppSign.buildMap(map);
        Observable<BookDetails> observable = NetUtils.getGsonRetrofit().bookShow(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }



    /**
     * 加载书柜
     *
     * @param bookIsbn
     */
    public void loadBookCase(String bookIsbn) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<BookCase>("bookCase") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {

            }

            @Override
            protected void onSuccess(BookCase response) {
                if (response.getJsonData() != null) {
                    getView().onBookCase(response);
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", bookIsbn);
        map.put("lat", MyApplicationLike.lat);
        map.put("lng", MyApplicationLike.lng);
        map.put("radius", "50");
        map = AppSign.buildMap(map);
        Observable<BookCase> observable = NetUtils.getGsonRetrofit().nearbycase(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);

    }

    /**
     * 图书所有者数据
     *
     * @param bookIsbn
     */
    public void loadBookPerson(String bookIsbn) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<BookPerson>("BookPerson") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {

            }

            @Override
            protected void onSuccess(BookPerson response) {
                if (response.getJsonData() != null) {
                    getView().onBookPerson(response);
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", bookIsbn);
        map = AppSign.buildMap(map);
        Observable<BookPerson> observable = NetUtils.getGsonRetrofit().bookPerson(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    /**
     * 加载看书的人
     *
     * @param bookIsbn
     */
    public void loadBookTalent(String bookIsbn) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<BookTalent>("talent") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {

            }

            @Override
            protected void onSuccess(BookTalent response) {
                if (response.getJsonData() != null) {
                    getView().onBookTalent(response);
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", bookIsbn);
        map = AppSign.buildMap(map);
        Observable<BookTalent> observable = NetUtils.getGsonRetrofit().commonReader(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 加载图书的评价
     *
     * @param bookIsbn
     */
    public void loadBookEval(String bookIsbn, String page) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<CommentGrade>("commonGrade") {
            @Override
            protected void onStart(Disposable d) {
                getView().onLoad();
            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(CommentGrade response) {
                if (response.getJsonData() != null) {
                    getView().onBookEval(response);
                } else {
                    getView().onEmpty();
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", bookIsbn);
        map.put("page", page);
        map = AppSign.buildMap(map);
        Observable<CommentGrade> observable = NetUtils.getGsonRetrofit().commonGrade(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 加载书友交流的评价
     *
     * @param bookIsbn
     */
    public void loadBookFriend(String bookIsbn, String page) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<CommentInfo>("commonInfo") {
            @Override
            protected void onStart(Disposable d) {
                getView().onLoad();
            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(CommentInfo response) {
                if (response.getJsonData() != null) {
                    getView().onBookFriend(response);
                } else {
                    getView().onEmpty();
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", bookIsbn);
        map.put("page", page);
        map = AppSign.buildMap(map);
        Observable<CommentInfo> observable = NetUtils.getGsonRetrofit().commonInfo(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);

    }

    /**
     * 点赞
     *
     * @param bookIsbn  isbn
     * @param type      1.评分,2.评论
     * @param commentId 评论的id
     */
    public void loadEvalPraise(String bookIsbn, final String type, String commentId) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("loadEvalPraise") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(Result response) {
                getView().onBookPraise(type);
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", bookIsbn);
        map.put("type", type);
        map.put("comment_id", commentId);

        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().commentPraise(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


}
