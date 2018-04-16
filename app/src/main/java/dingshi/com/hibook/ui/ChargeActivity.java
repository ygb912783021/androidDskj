package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.IPayMent;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Payment;
import dingshi.com.hibook.present.PayBookPresent;
import dingshi.com.hibook.share.EasyPayShare;
import dingshi.com.hibook.share.ShareCallBack;

/**
 * @author wangqi
 * @since 2017/12/21 下午3:44
 */

public class ChargeActivity extends BaseActivity implements IPayMent {

    public static final int CHARGE_REQUEST = 0x1129;

    @BindView(R.id.charge_money_ali_img)
    ImageView imgAliChoose;
    @BindView(R.id.charge_money_wx_img)
    ImageView imgWxChoose;
    @BindView(R.id.charge_money_tab1)
    TextView tab1;
    @BindView(R.id.charge_money_tab2)
    TextView tab2;
    @BindView(R.id.charge_money_tab3)
    TextView tab3;
    @BindView(R.id.charge_money_tab4)
    TextView tab4;


    PayBookPresent present = new PayBookPresent(this, this);
    int currentPay = 1;
    int currentPrice = 10;

    @Override
    public int getLayoutId() {
        return R.layout.activity_charge;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "充值", "");
    }

    @OnClick({R.id.charge_money_ali_layout, R.id.charge_money_wx_layout, R.id.charge_money_submit,
            R.id.charge_money_tab1, R.id.charge_money_tab2, R.id.charge_money_tab3, R.id.charge_money_tab4})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.charge_money_ali_layout:
                currentPay = 1;
                imgAliChoose.setVisibility(View.VISIBLE);
                imgWxChoose.setVisibility(View.GONE);
                break;
            case R.id.charge_money_wx_layout:
                currentPay = 2;
                imgAliChoose.setVisibility(View.GONE);
                imgWxChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.charge_money_submit:
                HashMap<String, String> map = new HashMap<>(2);
                map.put("order_type", "2");
                map.put("recharge_type", "3");
                map.put("pay_fee", String.valueOf(currentPrice));
                present.submit(map, currentPay);
                break;
            case R.id.charge_money_tab1:
                resetPrice(10);
                break;
            case R.id.charge_money_tab2:
                resetPrice(50);
                break;
            case R.id.charge_money_tab3:
                resetPrice(100);
                break;
            case R.id.charge_money_tab4:
                resetPrice(20);
                break;
            default:
        }
    }


    public void resetPrice(int currentPrice) {
        tab1.setBackgroundResource(R.drawable.charge_normal_border);
        tab2.setBackgroundResource(R.drawable.charge_normal_border);
        tab3.setBackgroundResource(R.drawable.charge_normal_border);
        tab4.setBackgroundResource(R.drawable.charge_normal_border);

        if (currentPrice == 10) {
            tab1.setBackgroundResource(R.drawable.charge_focus_border);
        } else if (currentPrice == 20) {
            tab4.setBackgroundResource(R.drawable.charge_focus_border);
        } else if (currentPrice == 50) {
            tab2.setBackgroundResource(R.drawable.charge_focus_border);
        } else if (currentPrice == 100) {
            tab3.setBackgroundResource(R.drawable.charge_focus_border);
        }

    }

    @Override
    public void onAli(Payment payment) {
        dismissProgressDialog();
        EasyPayShare.getInstance().doPayAli(payment.getJsonData().getSign(), this, new ShareCallBack() {

            @Override
            public void onSuccess(String result) {
                paySuccess();
            }

            @Override
            public void onCanceled(String result) {
                showToast("取消支付");
            }

            @Override
            public void onFailed(String result) {
                showToast("支付失败");
            }
        });
    }

    @Override
    public void onWx(Payment payment) {
        dismissProgressDialog();
        EasyPayShare.getInstance().doPayWx(this, payment.getJsonData(), new ShareCallBack() {
            @Override
            public void onSuccess(String result) {
                paySuccess();
            }

            @Override
            public void onCanceled(String result) {
                showToast("取消支付");
            }

            @Override
            public void onFailed(String result) {
                showToast("支付失败");
            }
        });
    }


    public void paySuccess() {
        showToast("支付成功");
        Intent intent = getIntent();
        setResult(CHARGE_REQUEST, intent);
        finish();
    }


    @Override
    public void onMoney(Payment payment) {

    }

    @Override
    public void onCoupon() {

    }

    @Override
    public void onError(String str) {
        dismissProgressDialog();
    }

    @Override
    public void start() {
        showProgressDialog("充值中...",true);
    }
}
