package dingshi.com.hibook.retrofit.observer;


import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.fuc.HttpResultFunction;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 适用Retrofit网络请求Observable(被监听者)
 *
 * @author ZhongDaFeng
 */
public class HttpRxObservable {

    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * data:网络请求参数
     * <h1>补充说明</h1>
     * 无管理生命周期,容易导致内存溢出
     *
     * @author ZhongDaFeng
     */
    public static Observable getObservable(Observable<? extends Result> apiObservable) {
        // showLog(request);
        Observable observable = apiObservable
//                .map(new ServerResultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }


    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * data:网络请求参数
     * <h1>补充说明</h1>
     * 传入LifecycleProvider<ActivityEvent>手动管理生命周期,避免内存溢出
     * 备注:需要继承RxActivity,RxAppCompatActivity,RxFragmentActivity
     *
     * @author ZhongDaFeng
     */
    public static Observable getObservable(Observable<? extends Result> apiObservable, LifecycleProvider<ActivityEvent> lifecycle, ActivityEvent event) {
        Observable observable;
        if (lifecycle != null) {
            observable = apiObservable
                    .compose(lifecycle.bindUntilEvent(event))
                    .onErrorResumeNext(new HttpResultFunction<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = getObservable(apiObservable);
        }
        return observable;
    }

    /**
     * fragment的请求
     *
     * @param apiObservable
     * @param lifecycle
     * @param event
     * @return
     */
    public static Observable getObservable(Observable<? extends Result> apiObservable, LifecycleProvider<FragmentEvent> lifecycle, FragmentEvent event) {
        Observable observable;
        if (lifecycle != null) {
            observable = apiObservable
                    .compose(lifecycle.bindUntilEvent(event))
                    .onErrorResumeNext(new HttpResultFunction<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = getObservable(apiObservable);
        }
        return observable;
    }


    public static Observable getResponseBody(Observable<ResponseBody> apiObservable, LifecycleProvider<ActivityEvent> lifecycle, ActivityEvent event) {
        // showLog(request);
        Observable observable;
        if (lifecycle != null) {
            //手动管理移除监听生命周期.eg:ActivityEvent.STOP
            observable = apiObservable
//                    .map(new ServerResultFunction())
                    .compose(lifecycle.bindUntilEvent(event))
                    .onErrorResumeNext(new HttpResultFunction<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = getResponseBody(apiObservable);
        }
        return observable;
    }


    public static Observable getResponseBody(Observable<ResponseBody> apiObservable) {
        // showLog(request);
        Observable observable = apiObservable
//                .map(new ServerResultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

}
