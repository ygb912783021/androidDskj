package dingshi.com.hibook.ui.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.MyViewPagerAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.view.SwitchViewPager;

/**
 * @author wangqi
 */
public class BookFriendFragment extends BaseFragment {

    @BindView(R.id.book_friend_switch)
    SwitchViewPager switchViewPager;
    @BindView(R.id.book_friend_viewpager)
    ViewPager viewPager;

    List<Fragment> list = new ArrayList<>();
    String[] title = new String[]{"推荐", "关注", "附近"};

    @Override
    public int getLayoutId() {
        return R.layout.fragment_book_friend;
    }

    @Override
    public void initView() {
        for (int i = 0; i < 3; i++) {
            Fragment frf = new FriendRecFragment();
            list.add(frf);
        }
        switchViewPager.bindViewPager(viewPager, title);
        viewPager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager(), list, title));

    }

}
