package dingshi.com.hibook.ui;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Coupon;
import dingshi.com.hibook.utils.AppManager;

/**
 * @author wangqi
 * @since 2017/11/14 上午9:51
 */


public class CouponDetailsActivity extends BaseActivity {
    @BindView(R.id.coupon_details_layout)
    ConstraintLayout couponLayout;
    @BindView(R.id.coupon_details_title)
    TextView txTitle;
    @BindView(R.id.coupon_details_describe)
    TextView txDescribe;
    @BindView(R.id.coupon_details_date)
    TextView txDate;
    @BindView(R.id.coupon_details_coupon)
    ImageView imgCoupon;


    Coupon.JsonDataBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_coupon_details;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "优惠券详情");
        bean = (Coupon.JsonDataBean) getIntent().getSerializableExtra("bean");
        txTitle.setText(bean.getName());
        txDescribe.setText(bean.getExplication());
        txDate.setText(String.format("有效期 %s~%s", bean.getStart_time(), bean.getEnd_time()));

        switch (bean.getPrice()) {
            case 1:
                couponLayout.setBackgroundResource(R.drawable.coupon_bg_1);
                imgCoupon.setImageResource(R.drawable.coupon_txt_1);
                break;
            case 2:
                couponLayout.setBackgroundResource(R.drawable.coupon_bg_2);
                imgCoupon.setImageResource(R.drawable.coupon_txt_2);
                break;
            case 5:
                couponLayout.setBackgroundResource(R.drawable.coupon_bg_5);
                imgCoupon.setImageResource(R.drawable.coupon_txt_5);
                break;
            case 10:
                couponLayout.setBackgroundResource(R.drawable.coupon_bg_10);
                imgCoupon.setImageResource(R.drawable.coupon_txt_10);
                break;
            default:
        }

    }


    @OnClick({R.id.coupon_details_submit})
    public void onClick(View view) {
        AppManager.getInstance().finishOthersActivity(MainActivity.class);
    }
}
