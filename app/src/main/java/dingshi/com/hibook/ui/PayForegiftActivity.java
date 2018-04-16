package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
import dingshi.com.hibook.utils.SpUtils;

/**
 * @author wangqi
 * @since 2018/1/9 下午2:16
 */
public class PayForegiftActivity extends BaseActivity implements IPayMent {
    @BindView(R.id.pay_foregift_ali_img)
    ImageView imgAliChoose;
    @BindView(R.id.pay_foregift_wx_img)
    ImageView imgWxChoose;

    PayBookPresent present = new PayBookPresent(this, this);

    int currentPay = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_foregift;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "押金充值");
    }


    @OnClick({R.id.pay_foregift_ali_layout, R.id.pay_foregift_wx_layout, R.id.pay_foregift_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_foregift_ali_layout:
                currentPay = 1;
                imgAliChoose.setVisibility(View.VISIBLE);
                imgWxChoose.setVisibility(View.GONE);
                break;
            case R.id.pay_foregift_wx_layout:
                currentPay = 2;
                imgAliChoose.setVisibility(View.GONE);
                imgWxChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.pay_foregift_submit:
                HashMap<String, String> map = new HashMap<>(2);
                map.put("order_type", "2");
                map.put("recharge_type", "4");
                present.submit(map, currentPay);
                break;
            default:
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
        //缓存到本地
        user.getJsonData().setCert_status(1);
        SpUtils.putUser(user);

        Intent intent = new Intent(this, ForegiftActivity.class);
        startActivity(intent);
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
        showProgressDialog("正在缴纳押金..", true);
    }
}
