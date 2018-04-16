package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.SpUtils;
import dingshi.com.hibook.view.TitleView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/11/7
 */


public class MoneyBagActivity extends BaseActivity {

    @BindView(R.id.money_bag_money)
    TextView txMoney;
    @BindView(R.id.money_bag_foregift)
    TextView txForegift;

    /**
     * 判断是都交过押金
     */
    private boolean isForegift;

    private String tip;

    @Override
    public int getLayoutId() {
        return R.layout.activity_money_bag;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "账户余额", "明细");
        getBalance();
    }

    @Override
    public void onRightClick() {
        startActivity(new Intent(MoneyBagActivity.this, MoneyDetailsActivity.class));
    }


    @OnClick({R.id.money_bag_charge, R.id.money_bag_balance, R.id.money_bag_foregift})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.money_bag_charge:
                Intent intent = new Intent(this, ChargeActivity.class);
                startActivityForResult(intent, ChargeActivity.CHARGE_REQUEST);
                break;
            case R.id.money_bag_balance:
                startActivity(new Intent(this, BackMoneyActivity.class));
                break;
            case R.id.money_bag_foregift:
                if (SpUtils.getUser().getJsonData().getBalance_refund_status() == 0) {
                    startActivity(new Intent(this, isForegift ? ForegiftActivity.class : PayForegiftActivity.class));
                }
                break;
            default:
        }
    }

    /**
     * 获取余额
     */
    public void getBalance() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<User>("userShow") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(User u) {
                if (u.getJsonData() != null) {
                    updateUI(u);
                    //余额
                    user.getJsonData().setBalance(u.getJsonData().getBalance());
                    //押金状态
                    user.getJsonData().setCert_status(u.getJsonData().getCert_status());
                    //更新到缓存
                    SpUtils.putUser(user);
                    EventBus.getDefault().post(user);
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("uid", user.getJsonData().getUser_id());
        map = AppSign.buildMap(map);
        Observable<User> user = NetUtils.getGsonRetrofit().userShow(map);
        HttpRxObservable.getObservable(user, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    public void updateUI(User u) {
        txMoney.setText(u.getJsonData().getBalance());

        if (u.getJsonData().getBalance_refund_status() == 0) {
            if (u.getJsonData().getCert_status() == 1) {
                isForegift = true;
                tip = "已交押金，点击查看";
            } else {
                isForegift = false;
                tip = "未交押金，点击充值";
            }
            SpannableString str = new SpannableString(tip);
            ForegroundColorSpan fore = new ForegroundColorSpan(0xff3273fe);
            str.setSpan(fore, 5, str.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
            txForegift.setText(str);
        } else {
            txForegift.setText("押金退款审核中");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateUI(SpUtils.getUser());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        //更新当前页面的余额
        //通知我的页面
        if (requestCode == ChargeActivity.CHARGE_REQUEST) {
            getBalance();
        }
    }
}
