package dingshi.com.hibook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import dingshi.com.hibook.base.BaseFragment;

/**
 * @author wangqi
 *         Created by apple on 2017/10/27.
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList;
    private String[] title;

    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] title) {
        super(fm);
        this.fragmentList = fragmentList;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
