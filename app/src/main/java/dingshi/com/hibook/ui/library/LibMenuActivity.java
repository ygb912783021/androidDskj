package dingshi.com.hibook.ui.library;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.MyViewPagerAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.ui.fragment.BookListFragment;
import dingshi.com.hibook.ui.fragment.LibUserFragment;
import dingshi.com.hibook.view.SwitchViewPager;

/**
 * @author wangqi
 * @since 2017/12/22 下午2:44
 */

public class LibMenuActivity extends BaseActivity {
    @BindView(R.id.lib_menu_switch)
    SwitchViewPager switchViewPager;
    @BindView(R.id.lib_menu_viewpager)
    ViewPager viewPager;

    List<Fragment> list = new ArrayList<>();

    String[] title = new String[]{"用户", "图书列表"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_lib_menu;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String catalogId = getIntent().getStringExtra("catalog_id");
        requestActionBarStyle(true, "图书馆名单", "");
        switchViewPager.bindViewPager(viewPager, title);
        Fragment userFragment = new LibUserFragment();
        Bundle b1 = new Bundle();
        b1.putString("catalog_id", catalogId);
        userFragment.setArguments(b1);
        list.add(userFragment);

        Fragment baseFragment = new BookListFragment();
        Bundle b2 = new Bundle();
        b2.putString("catalog_id", catalogId);
        baseFragment.setArguments(b1);
        list.add(baseFragment);


        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), list, title));
    }
}
