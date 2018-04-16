package dingshi.com.hibook.present;

import android.util.Log;

import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.HashMap;

import dingshi.com.hibook.MyApplicationLike;
import dingshi.com.hibook.action.IRequestView;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Home;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/11 17:01
 */

public class BookStorePresent extends BasePresent<IRequestView<Home>, BaseFragment> {
    public BookStorePresent(IRequestView<Home> view, BaseFragment activity) {
        super(view, activity);
    }

    public void onLoad() {
        HashMap<String, String> map = new HashMap<>();
        map.put("lat", MyApplicationLike.lat);
        map.put("lng", MyApplicationLike.lng);
        map = AppSign.buildMap(map);

        HttpRxObserver httpRxObserver = new HttpRxObserver<Home>("home") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError("加载失败");
            }

            @Override
            protected void onSuccess(Home home) {
                if (home.getJsonData() != null) {
                    getView().onSuccess(home);
                }
            }
        };

        Observable<Home> home = NetUtils.getGsonRetrofit().home(map);
        HttpRxObservable.getObservable(home, getActivity(), FragmentEvent.PAUSE).subscribe(httpRxObserver);

    }
}
