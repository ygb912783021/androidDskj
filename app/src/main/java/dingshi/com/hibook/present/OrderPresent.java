package dingshi.com.hibook.present;

import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.HashMap;

import dingshi.com.hibook.action.IOrderView;
import dingshi.com.hibook.action.IRequestView;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Order;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.share.EasyPayShare;
import dingshi.com.hibook.share.PayWeixin;
import dingshi.com.hibook.share.ShareCallBack;
import dingshi.com.hibook.ui.fragment.OrderFragment;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/11/14 11:19
 */

public class OrderPresent extends BasePresent<IRequestView, OrderFragment> {
    public OrderPresent(IRequestView view, OrderFragment activity) {
        super(view, activity);
    }

    public void load(String userId, int paymentStatus, int page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("payment_status", String.valueOf(paymentStatus));
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);
        HttpRxObserver httpRxObserver = new HttpRxObserver<Order>("orderAll") {
            @Override
            protected void onStart(Disposable d) {
                getView().onLoad();
            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Order user) {
                if (user.getJsonData() == null) {
                    getView().onEmpty();
                } else {
                    getView().onSuccess(user);
                }
            }
        };
        Observable<Order> user = NetUtils.getGsonRetrofit().orderAll(map);
        HttpRxObservable.getObservable(user, getActivity(), FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * @param tradePlatform 1、支付宝  2、微信
     * @param sign
     */
    public void pay(int tradePlatform, PayWeixin sign) {
        if (tradePlatform == 1) {
            payAli(sign);
        } else if (tradePlatform == 2) {
            payWx(sign);
        }
    }

    /**
     * 微信支付
     *
     * @param sign
     */
    public void payWx(PayWeixin sign) {
        EasyPayShare.getInstance().doPayWx(getActivity().mActivity, sign, new ShareCallBack() {
            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onCanceled(String result) {
            }

            @Override
            public void onFailed(String result) {
            }
        });
    }

    /**
     * 支付宝支付
     *
     * @param sign
     */
    public void payAli(PayWeixin sign) {


        EasyPayShare.getInstance().doPayAli(sign.getSign(), getActivity().mActivity, new ShareCallBack() {

            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onCanceled(String result) {
            }

            @Override
            public void onFailed(String result) {
            }
        });
    }


}
