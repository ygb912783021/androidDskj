package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.SpUtils;
import dingshi.com.hibook.view.FuckDialog;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2018/1/9 上午11:28
 */
public class ForegiftActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_foregift;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        hideTitleBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    @OnClick({R.id.foregift_quit, R.id.foregift_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.foregift_back:
                finish();
                break;
            case R.id.foregift_quit:
                showDialog();
                break;
            default:
        }
    }


    /**
     *
     */
    FuckDialog dialog;

    public void showDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_foregift, null, false);
        dialog = new FuckDialog(this).builder().addView(view).builder();
        dialog.show();
        view.findViewById(R.id.dialog_foregift_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dissmis();
                quitForegift();
            }
        });
        view.findViewById(R.id.dialog_foregift_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dissmis();
            }
        });
    }

    /**
     * 退押金
     */
    public void quitForegift() {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("amount", "29");
        map.put("type", "1");
        map = AppSign.buildMap(map);

        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("returnMoney") {
            @Override
            protected void onStart(Disposable d) {
                showProgressDialog("退款中...", true);
            }

            @Override
            protected void onError(ApiException e) {
                showToast(e.getMsg());
                dismissProgressDialog();
            }

            @Override
            protected void onSuccess(Result response) {
                dismissProgressDialog();
                //将本地的记录设置成已退押金状态
                user.getJsonData().setCert_status(0);
                SpUtils.putUser(user);

                startActivity(new Intent(ForegiftActivity.this, RefundActivity.class));

                finish();


            }
        };
        Observable<Result> observable = dingshi.com.hibook.retrofit.net.NetUtils.getGsonRetrofit().returnMoney(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


}
