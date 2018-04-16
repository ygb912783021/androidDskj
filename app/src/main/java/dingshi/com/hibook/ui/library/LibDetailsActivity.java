package dingshi.com.hibook.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.MyViewPagerAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.lib.LibCreate;
import dingshi.com.hibook.bean.lib.LibList;
import dingshi.com.hibook.eventbus.LibDeleteEvent;
import dingshi.com.hibook.eventbus.LibRefreshEvent;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.fragment.BookListFragment;
import dingshi.com.hibook.ui.fragment.LibIntroFragment;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.view.SwitchViewPager;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/20 下午4:07
 */


public class LibDetailsActivity extends BaseActivity {
    @BindView(R.id.lib_details_tab)
    SwitchViewPager switchViewPager;
    @BindView(R.id.lib_details_vp)
    ViewPager viewPager;

    List<Fragment> list = new ArrayList<>();
    String[] title = new String[]{"介绍", "图书列表"};

    String catalogId;


    LibList.JsonDataBean libBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lib_details;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        catalogId = getIntent().getStringExtra("catalog_id");
        updataUI();
        showLib();
    }


    public void showLib() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<LibCreate>("LibCreate") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(LibCreate libIntro) {
                if (libIntro.getJsonData() != null) {
                    libBean = libIntro.getJsonData();
                    cardInfo.setIntro(libBean);
                }
            }
        };

        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalogId);
        map = AppSign.buildMap(map);
        Observable<LibCreate> user = NetUtils.getGsonRetrofit().libIntro(map);
        HttpRxObservable.getObservable(user, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    LibIntroFragment cardInfo;

    public void updataUI() {
        requestActionBarStyle(true, "", "管理后台");

        //介绍
        cardInfo = new LibIntroFragment();
        Bundle b = new Bundle();
        b.putString("catalog_id", catalogId);
        cardInfo.setArguments(b);
        list.add(cardInfo);

        //图书列表
        BookListFragment booList = new BookListFragment();
        Bundle b2 = new Bundle();
        b2.putString("catalog_id", catalogId);
        booList.setArguments(b2);
        list.add(booList);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), list, title));
        switchViewPager.bindViewPager(viewPager, title);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 删除图书馆操作通知
     */
    @Subscribe
    public void libDelete(LibDeleteEvent libEvent) {
        finish();
    }

    /**
     * 刷新图书馆
     */
    @Subscribe
    public void libRefresh(LibRefreshEvent libEvent) {
        showLib();
    }


    @Override
    public void onRightClick() {
        if (libBean == null) {
            return;
        }
        Intent intent = new Intent(this, LibManagerActivity.class);
        libBean.setCatalog_id(catalogId);
        intent.putExtra("bean", libBean);
        intent.putExtra("isRally", false);
        startActivity(intent);
    }


}
