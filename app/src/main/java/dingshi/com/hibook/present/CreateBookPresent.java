package dingshi.com.hibook.present;

import com.google.gson.Gson;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import dingshi.com.hibook.action.ICreateBook;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.Douban;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.share.HttpUtils;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author wangqi
 * @since 2017/12/18 13:26
 */

public class CreateBookPresent extends BasePresent<ICreateBook, BaseActivity> {
    public CreateBookPresent(ICreateBook view, BaseActivity activity) {
        super(view, activity);
    }

    /**
     * 从服务器获取数据
     *
     * @param isbn
     */
    public void getBook(final String isbn) {

        HttpRxObserver httpRxObserver = new HttpRxObserver<BookDetails>("bookShow") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError("获取图书失败");
            }

            @Override
            protected void onSuccess(BookDetails response) {
                if (response.getError_code() == -1) {
                    getDouBanBook(isbn);
                } else if (response.getJsonData() != null) {
                    getView().getBook(response);
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", isbn);
        map = AppSign.buildMap(map);
        Observable<BookDetails> observable = NetUtils.getGsonRetrofit().bookShow(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);


//        HttpRxObserver httpRxObserver = new HttpRxObserver<ResponseBody>("bookScan") {
//            @Override
//            protected void onStart(Disposable d) {
//
//            }
//
//            @Override
//            protected void onError(ApiException e) {
//                getView().onError("获取图书失败");
//            }
//
//            @Override
//            protected void onSuccess(ResponseBody response) {
//                try {
//                    Zxing zxing = new Gson().fromJson(response.string(), Zxing.class);
//                    //没有数据时为-1
//                    if (zxing.getError_code() == -1) {
//                        getDouBanBook(isbn);
//                    } else {
//                        getView().getBook(zxing);
//                    }
//                } catch (Exception e) {
//                    getDouBanBook(isbn);
//                }
//            }
//        };
//        HashMap<String, String> map = new HashMap<>(2);
//        map.put("isbn", isbn);
//        map = AppSign.buildMap(map);
//        Observable<ResponseBody> observable = NetUtils.getGsonRetrofit().bookScan(map);
//        HttpRxObservable.getResponseBody(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 从豆瓣获取图书
     *
     * @param isbn
     */
    public void getDouBanBook(final String isbn) {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                String text = HttpUtils.getHtml("https://api.douban.com/v2/book/isbn/:" + isbn);
                e.onNext(text);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        getView().getDouban(new Gson().fromJson(s, Douban.class));
                    }
                });
    }


    public void addBooks(String books) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<ResponseBody>("addBooks") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError("添加图书出错");
            }

            @Override
            protected void onSuccess(ResponseBody response) {
                try {
                    Result r = new Gson().fromJson(response.string(), Result.class);
                    if (r.getError_code() == -1) {
                        getView().onSuccess(null);
                    }
                } catch (Exception e) {
                    getView().onError("添加图书出错");
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map = AppSign.buildMap(map);
        Observable<ResponseBody> observable = NetUtils.getStringRetrofit().bookaddAll(map, books);
        HttpRxObservable.getResponseBody(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);

//
//        HashMap<String, String> map = new HashMap<>(2);
//        map = AppSign.buildMap(map);
//        NetUtils.getStringRetrofit().bookaddAll(map, books, SpUtils.getUser().getToken()).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResponseBody>() {
//            @Override
//            public void accept(@NonNull ResponseBody responseBody) throws Exception {
//                Log.i("tag", responseBody.string());
//            }
//        });

    }

}
