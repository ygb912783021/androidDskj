package dingshi.com.hibook.present;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;
import java.util.TreeMap;

import dingshi.com.hibook.action.IPayMent;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Payment;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/14 13:49
 */

public class PayBookPresent extends BasePresent<IPayMent, BaseActivity> {

    public PayBookPresent(IPayMent view, BaseActivity activity) {
        super(view, activity);
    }

    /**
     * @param map       支付参数
     * @param payMethod 支付平台
     */
    public void submit(HashMap<String, String> map, final int payMethod) {

        HashMap<String, String> params = new HashMap<>(2);
        params.putAll(map);
        params.put("trade_platform", String.valueOf(payMethod));

        params = AppSign.buildMap(params);
        HttpRxObserver httpRxObserver = new HttpRxObserver<Payment>("payment") {
            @Override
            protected void onStart(Disposable d) {
                getView().start();
            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Payment payment) {
                if (payment.getJsonData() != null) {
                    if (payMethod == 1) {
                        getView().onAli(payment);
                    } else if (payMethod == 2) {
                        getView().onWx(payment);
                    } else if (payMethod == 3) {
                        getView().onMoney(payment);
                    }
                }
            }
        };

        Observable<Payment> home = NetUtils.getGsonRetrofit().payment(params);
        HttpRxObservable.getObservable(home, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


}
