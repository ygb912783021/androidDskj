package dingshi.com.hibook.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.SpUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/21 下午3:46
 */


public class BackMoneyActivity extends BaseActivity {

    @BindView(R.id.back_money_price)
    TextView txPrice;
    String strPrice;

    @Override
    public int getLayoutId() {
        return R.layout.activity_back_money;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "退余额", "");
        strPrice = user.getJsonData().getBalance();
        txPrice.setText(strPrice);
    }


    @OnClick({R.id.back_money_cancel, R.id.back_money_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_money_cancel:
                finish();
                break;
            case R.id.back_money_ok:
                returnMoney();
                break;
            default:
        }
    }

    public void returnMoney() {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("amount", strPrice);
        map.put("type", "2");
        map = AppSign.buildMap(map);

        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("returnMoney") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
                showToast(e.getMsg());
            }

            @Override
            protected void onSuccess(Result response) {
                showToast("退款申请成功");
            }
        };

        Observable<Result> observable = dingshi.com.hibook.retrofit.net.NetUtils.getGsonRetrofit().returnMoney(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
}
