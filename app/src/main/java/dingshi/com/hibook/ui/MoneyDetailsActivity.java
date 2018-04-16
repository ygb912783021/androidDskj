package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.MyViewPagerAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.ui.fragment.ChargeDetailsFragment;
import dingshi.com.hibook.ui.fragment.MoneyDetailsFragment;
import dingshi.com.hibook.view.SwitchViewPager;
import dingshi.com.hibook.view.TitleView;

/**
 * @author wangqi
 * @since 2017/11/7
 */

public class MoneyDetailsActivity extends BaseActivity {

    @BindView(R.id.money_detail_switch)
    SwitchViewPager switchViewPager;
    @BindView(R.id.money_detail_viewpager)
    ViewPager viewPager;

    String[] title = new String[]{"消费", "充值", "收入"};
    List<Fragment> list = new ArrayList<>();
    BaseFragment[] fragments = new BaseFragment[]{new MoneyDetailsFragment(), new ChargeDetailsFragment(), new ChargeDetailsFragment()};

    @Override
    public int getLayoutId() {
        return R.layout.activity_money_details;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "余额明细", "");
        switchViewPager.bindViewPager(viewPager, title);

        for (int i = 0; i < title.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("genre", i + 1);
            Fragment bbf = fragments[i];
            bbf.setArguments(bundle);
            list.add(bbf);
        }

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), list,title));
        titleView.setOnFinishListener(new TitleView.OnFinishListener() {
            @Override
            public void over() {
                MoneyDetailsActivity.this.finish();
            }
        });
    }
}
