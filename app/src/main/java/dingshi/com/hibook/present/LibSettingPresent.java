package dingshi.com.hibook.present;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import dingshi.com.hibook.action.ILibSettingView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2018/1/29 18:25
 */

public class LibSettingPresent extends BasePresent<ILibSettingView, BaseActivity> {
    public LibSettingPresent(ILibSettingView view, BaseActivity activity) {
        super(view, activity);
    }


    /**
     * 设置人数上限
     *
     * @param catalogId
     * @param persons
     */
    public void setLimitPerson(String catalogId, String persons) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("libLimitPerson") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Result response) {
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalogId);
        map.put("user_limit", persons);
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().libLimitPerson(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.DESTROY).subscribe(httpRxObserver);
    }

    /**
     * 设置虚拟人数
     *
     * @param catalogId
     * @param persons
     */
    public void setFakePerson(String catalogId, String persons) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("libFakePerson") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Result response) {
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalogId);
        map.put("fake_user", persons);
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().libFakePerson(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.DESTROY).subscribe(httpRxObserver);
    }


    /**
     * 图书馆允许加入设置
     *
     * @param catalogId
     * @param isOpen    0：不允许加入 1：允许加入
     */
    public void setJoin(String catalogId, boolean isOpen) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("libAllow") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Result response) {
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalogId);
        map.put("is_allow", isOpen ? "1" : "0");
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().libAllow(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.DESTROY).subscribe(httpRxObserver);

    }

    /**
     * 允许用户咨询设置
     *
     * @param catalogId
     * @param isOpen    1：允许 2：不允许 默认为1
     */
    public void setConsult(String catalogId, boolean isOpen) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("libConsult") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Result response) {
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalogId);
        map.put("is_consult", isOpen ? "1" : "2");
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().libConsult(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.DESTROY).subscribe(httpRxObserver);
    }

    /**
     * 通知提醒设置
     * 1：加入后消息提醒 2：加入后不提醒 默认为1
     *
     * @param catalogId
     * @param isOpen
     */
    public void setNotice(String catalogId, boolean isOpen) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("libNotice") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Result response) {
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalogId);
        map.put("message_state", isOpen ? "1" : "2");
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().libNotice(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.DESTROY).subscribe(httpRxObserver);
    }


    /**
     * 公开到图书馆
     * 1：公开 2：不公开 默认为1
     *
     * @param catalogId
     * @param isOpen
     */
    public void setOpen(String catalogId, boolean isOpen) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("libOpen") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Result response) {
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalogId);
        map.put("is_open", isOpen ? "1" : "2");
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().libOpen(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.DESTROY).subscribe(httpRxObserver);
    }

}
