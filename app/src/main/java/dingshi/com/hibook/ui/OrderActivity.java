package dingshi.com.hibook.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.MyViewPagerAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.ui.fragment.MoneyDetailsFragment;
import dingshi.com.hibook.ui.fragment.OrderFragment;
import dingshi.com.hibook.utils.AppManager;
import dingshi.com.hibook.view.SwitchViewPager;
import dingshi.com.hibook.view.TitleView;

/**
 * @author wangqi
 * @since 2017/11/7 上午11:16
 */


public class OrderActivity extends BaseActivity {

    @BindView(R.id.order_switch)
    SwitchViewPager switchViewPager;
    @BindView(R.id.order_viewpager)
    ViewPager viewPager;

    String[] title = new String[]{"全部", "待支付", "已支付"};
    int[] payStatus = new int[]{0, 2, 1};
    List<Fragment> list = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "我的订单", "");
        switchViewPager.bindViewPager(viewPager, title);

        for (int i = 0; i < title.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("payment_status", payStatus[i]);
            Fragment bbf = new OrderFragment();
            bbf.setArguments(bundle);
            list.add(bbf);
        }

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), list,title));

        titleView.setOnFinishListener(new TitleView.OnFinishListener() {
            @Override
            public void over() {
                OrderActivity.this.finish();
            }
        });
    }


    @Override
    public void onLeftClick() {
        super.onLeftClick();
        finishPage();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishPage();
    }

    /**
     * 如果当前是打开书柜跳的订单页，需要跳转到我的借阅的tab2
     */
    public void finishPage() {
        if (getIntent().getBooleanExtra("borrow", false)) {
            AppManager.getInstance().finishOthersActivity(MainActivity.class);
            Intent intent = new Intent(OrderActivity.this, MyBorrowActivity.class);
            startActivity(intent);
        }
    }
}
