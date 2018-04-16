package dingshi.com.hibook.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.MyViewPagerAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.ui.fragment.BackBookFragment;
import dingshi.com.hibook.view.SwitchViewPager;

/**
 * 我的借阅
 *
 * @author wangqi
 * @since 2017-11-02 5:30pm
 */
public class MyBorrowActivity extends BaseActivity {
    @BindView(R.id.borrow_switch)
    SwitchViewPager switchViewPager;
    @BindView(R.id.borrow_viewpager)
    ViewPager viewPager;


    String[] title = new String[]{"借书列表", "还书列表"};
    List<Fragment> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_borrow;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "我的借阅", "");
        switchViewPager.bindViewPager(viewPager, title);
        for (int i = 0; i < title.length; i++) {
            BackBookFragment bbf = new BackBookFragment();
            bbf.setTitle(title[i]);
            Bundle bundle = new Bundle();
            bundle.putInt("returned", i);
            bbf.setArguments(bundle);
            list.add(bbf);
        }
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), list, title));

        if (getIntent().getBooleanExtra("switch", false)) {
            viewPager.setCurrentItem(1, false);
        }
    }
    @OnClick({R.id.book_house_zxing})
    public void onClick(View v) {
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.CAMERA).callback(new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                if (requestCode == 200) {
                    startActivity(new Intent(MyBorrowActivity.this, ZxingActivity.class));
                }
            }

            @Override
            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

            }
        }).start();
    }
}
