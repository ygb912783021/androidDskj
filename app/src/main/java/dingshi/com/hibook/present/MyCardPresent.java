package dingshi.com.hibook.present;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import dingshi.com.hibook.action.IMyCard;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.card.CardList;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2018/3/8 14:33
 */

public class MyCardPresent extends BasePresent<IMyCard, BaseActivity> {

    public MyCardPresent(IMyCard view, BaseActivity activity) {
        super(view, activity);
    }

    /**
     * 获取卡列表
     */
    public void getCardList() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<CardList>("cardList") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(CardList cardList) {
                getView().onCardList(cardList);
            }
        };

        HashMap<String, String> map = new HashMap<>(2);
        map = AppSign.buildMap(map);
        Observable<CardList> observable = NetUtils.getGsonRetrofit().cardList(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    /**
     * 删除卡片
     * @param cardId
     */
    public void deleteCard(String cardId) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("cardDelete") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Result cardList) {
                getView().onDeleteCard();
            }
        };

        HashMap<String, String> map = new HashMap<>(2);
        map.put("card_id", cardId);
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().cardDelete(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
}
