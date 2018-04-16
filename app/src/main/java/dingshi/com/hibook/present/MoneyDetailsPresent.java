package dingshi.com.hibook.present;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.HashMap;

import dingshi.com.hibook.action.IRequestView;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.MoneyDetails;
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
 * @since 2017/11/13 16:54
 */

public class MoneyDetailsPresent extends BasePresent<IRequestView, BaseFragment> {

    public MoneyDetailsPresent(IRequestView view, BaseFragment activity) {
        super(view, activity);
    }


    public void loadData(int genre, int page, String userId) {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("user_id", userId);
        map.put("genre", String.valueOf(genre));
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);

        HttpRxObserver httpRxObserver = new HttpRxObserver<MoneyDetails>("receipt") {
            @Override
            protected void onStart(Disposable d) {
                getView().onLoad();
            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(MoneyDetails response) {
                if (response.getError_code() == -1) {
                    getView().onEmpty();
                } else {
                    getView().onSuccess(response);
                }
            }
        };
        Observable<MoneyDetails> observable = NetUtils.getGsonRetrofit().receipt(map);
        HttpRxObservable.getObservable(observable, getActivity(), FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }
}
