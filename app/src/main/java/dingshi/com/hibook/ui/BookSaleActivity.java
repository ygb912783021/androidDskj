package dingshi.com.hibook.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.Constant;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.MyViewPagerAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.eventbus.EventBusHelper;
import dingshi.com.hibook.ui.fragment.BookSaleFragment;
import dingshi.com.hibook.ui.fragment.OrderFragment;
import dingshi.com.hibook.view.SwitchViewPager;

public class BookSaleActivity extends BaseActivity {
    @BindView(R.id.sale_switch)
    SwitchViewPager sale_switch;
    @BindView(R.id.sale_viewpager)
    ViewPager sale_viewpager;
    String[] title = new String[]{"新书售卖", "二手书售卖", "电子书售卖"};
    List<Fragment> list = new ArrayList<>();
    int[] payStatus = new int[]{0, 2, 1};
    @Override
    protected void initView(Bundle savedInstanceState) {
//        Constant.isEditDelete=false;
//        EventBusHelper.register(this);
        requestActionBarStyle(true, "图书售卖", "");
        sale_switch.bindViewPager(sale_viewpager,title);
        for (int i = 0; i < title.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("payment_status", payStatus[i]);
            Fragment bbf = new BookSaleFragment();
            bbf.setArguments(bundle);
            list.add(bbf);
        }
        sale_viewpager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), list,title));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sale;
    }
}
