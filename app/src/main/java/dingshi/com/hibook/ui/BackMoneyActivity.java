package dingshi.com.hibook.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeui.widget.EaseAlertDialog;
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
                checkBalanceAndReturn();
                break;
            default:
        }
    }

    private void checkBalanceAndReturn() {
        if (TextUtils.isEmpty(strPrice)) {
            showNoBalanceNoticeDialog();
        } else {

            double balance = Double.valueOf(strPrice).doubleValue();
            if (balance > 0) {
                returnMoney();

            } else {
                showNoBalanceNoticeDialog();
            }
        }

    }

    private void showNoBalanceNoticeDialog() {
        new EaseAlertDialog(this, "您还没有余额，不能申请退款").show();

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
