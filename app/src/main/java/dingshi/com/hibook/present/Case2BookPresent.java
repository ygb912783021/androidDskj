package dingshi.com.hibook.present;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import dingshi.com.hibook.action.ICase2BookView;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Case2Book;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.Case2BookActivity;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/15 15:31
 */

public class Case2BookPresent extends BasePresent<ICase2BookView, Case2BookActivity> {
    public Case2BookPresent(ICase2BookView view, Case2BookActivity activity) {
        super(view, activity);
    }


    public void onLoad(String serialNumber) {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("serial_number", serialNumber);
        map = AppSign.buildMap(map);

        HttpRxObserver httpRxObserver = new HttpRxObserver<Case2Book>("bookList") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(Case2Book result) {
                if (result.getJsonData() != null) {
                    getView().onSuccess(result);
                }
            }
        };

        Observable<Case2Book> observable = dingshi.com.hibook.retrofit.net.NetUtils.getGsonRetrofit().bookList(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);

    }
}
