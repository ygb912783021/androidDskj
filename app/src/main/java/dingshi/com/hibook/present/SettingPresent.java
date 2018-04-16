package dingshi.com.hibook.present;

import android.widget.Toast;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import dingshi.com.hibook.action.ISettingView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.SettingActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.SpUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/11/9 16:43
 */

public class SettingPresent extends BasePresent<ISettingView, BaseActivity> {

    public SettingPresent(ISettingView view, BaseActivity activity) {
        super(view, activity);
    }


    /**
     * 退出登录
     */
    public void signOut() {

        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("signOut") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(Result response) {
                getView().signOut();
            }
        };

        HashMap<String, String> map = new HashMap<>(2);
        map.put("user_id", SpUtils.getUser().getJsonData().getUser_id());
        map = AppSign.buildMap(map);

        Observable<Result> user = NetUtils.getGsonRetrofit().signOut(map);
        HttpRxObservable.getObservable(user, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);

    }

}
