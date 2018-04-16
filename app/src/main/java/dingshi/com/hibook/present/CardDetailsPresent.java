package dingshi.com.hibook.present;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.trello.rxlifecycle2.android.ActivityEvent;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import dingshi.com.hibook.Constant;
import dingshi.com.hibook.action.ICardDetails;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.card.CardDetails;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wangqi
 * @since 2017/12/20 11:24
 */

public class CardDetailsPresent extends BasePresent<ICardDetails, BaseActivity> {
    public CardDetailsPresent(ICardDetails view, BaseActivity activity) {
        super(view, activity);
    }

    /**
     * 获取卡片详情
     */
    public void getCardDetails(HashMap<String, String> map) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<CardDetails>("cardShow") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {

            }

            @Override
            protected void onSuccess(CardDetails response) {
                getView().onCardDetails(response);
            }
        };
        map = AppSign.buildMap(map);
        Observable<CardDetails> observable = NetUtils.getGsonRetrofit().cardShow(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 设置主名片
     *
     * @param cardId 卡片id
     */
    public void setMainCard(String cardId) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("setMainCard") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {

            }

            @Override
            protected void onSuccess(Result response) {
                getView().onMainCard();
            }
        };
        /**
         *   还需要设置两个图片
         */

        HashMap<String, String> map = new HashMap<>(2);
        map.put("card_id", cardId);
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().setMainCard(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);

    }

    /**
     * 交换名片申请
     * <p>
     * 对方的用户id
     *
     * @param toUserId
     */
    public void sweepCard(String toUserId) {

        HttpRxObserver httpRxObserver = new HttpRxObserver<CardDetails>("cardApply") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(CardDetails response) {
                getView().onSweep(response);
            }
        };
        /**
         *   还需要设置两个图片
         */
        HashMap<String, String> map = new HashMap<>(2);
        map.put("uid", toUserId);
        map = AppSign.buildMap(map);
        Observable<CardDetails> observable = NetUtils.getGsonRetrofit().cardApply(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    public void loadZxing(final String userId, final String cardId) {
        Flowable.create(new FlowableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(FlowableEmitter<Bitmap> e) throws Exception {
                Bitmap logoBitmap = QRCodeEncoder.syncEncodeQRCode(Constant.getShareCardUrl(userId, cardId), BGAQRCodeUtil.dp2px(getActivity(), 150), Color.BLACK);
                e.onNext(logoBitmap);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        if (bitmap != null) {
                            getView().onLoadZxing(bitmap);
                        } else {
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
