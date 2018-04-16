package dingshi.com.hibook.present;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import dingshi.com.hibook.action.IRequestView;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Coupon;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.CouponActivity;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/11/14 09:01
 */

public class CouponPresent extends BasePresent<IRequestView, CouponActivity> {
    public CouponPresent(IRequestView view, CouponActivity activity) {
        super(view, activity);
    }


    public void refresh(User user, int page) {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("user_id", String.valueOf(user.getJsonData().getUser_id()));
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);

        HttpRxObserver httpRxObserver = new HttpRxObserver<Coupon>("couponAll") {
            @Override
            protected void onStart(Disposable d) {
                getView().onLoad();
            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Coupon response) {
                if (response.getJsonData() == null) {
                    getView().onEmpty();
                } else {
                    getView().onSuccess(response);
                }
            }
        };

        Observable<Coupon> observable = dingshi.com.hibook.retrofit.net.NetUtils.getGsonRetrofit().couponAll(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);


    }
}
