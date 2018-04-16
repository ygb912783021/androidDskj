package dingshi.com.hibook.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.IRequestView;
import dingshi.com.hibook.adapter.StoreMultiAdapter;
import dingshi.com.hibook.adapter.StoreMultipleItem;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.Home;
import dingshi.com.hibook.present.BookStorePresent;
import dingshi.com.hibook.ui.BookHouseActivity;
import dingshi.com.hibook.ui.MyBorrowActivity;
import dingshi.com.hibook.ui.SearchActivity;

/**
 * @author wangqi
 */
public class BookStoreFragment extends BaseFragment implements IRequestView<Home> {

    @BindView(R.id.smartrefresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.book_store_recycleview)
    RecyclerView mRecyclerView;
    @BindView(R.id.book_store_toolbar)
    LinearLayout toolbar;

    @BindView(R.id.book_store_backbook)
    ImageView imgBackBook;

    StoreMultiAdapter multiItemAdapter;
    List<StoreMultipleItem> list = new ArrayList<>();

    BookStorePresent present = new BookStorePresent(this, this);

    @Override
    public int getLayoutId() {
        return R.layout.fragment_book_store;
    }

    @Override
    public void initView() {

        present.onLoad();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        multiItemAdapter = new StoreMultiAdapter(mActivity, list);
        mRecyclerView.setAdapter(multiItemAdapter);
        smartRefreshLayout.setEnableLoadmore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                present.onLoad();
            }
        });

        /**
         * 滑动监听
         * 滑动的距离超过第一个item就设置不透明
         * */
        toolbar.getBackground().mutate().setAlpha(0);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (manager.findFirstVisibleItemPosition() > 0) {
                    toolbar.getBackground().mutate().setAlpha(255);
                    imgBackBook.setImageResource(R.drawable.back_book_release);
                } else {
                    toolbar.getBackground().mutate().setAlpha(0);
                    imgBackBook.setImageResource(R.drawable.back_book_normal);
                }
            }
        });
    }


    @OnClick({R.id.book_store_backbook, R.id.book_store_search, R.id.book_store_zxing})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_store_backbook:
                startActivity(new Intent(mActivity, MyBorrowActivity.class));
                break;
            case R.id.book_store_search:
                startActivity(new Intent(mActivity, SearchActivity.class));
                break;
            case R.id.book_store_zxing:
                startActivity(new Intent(mActivity, BookHouseActivity.class));
                break;
            default:
        }

    }


    /**
     * 请求成功
     *
     * @param home
     */
    @Override
    public void onSuccess(Home home) {
        list.clear();
        smartRefreshLayout.finishRefresh();
        Home.JsonDataBean bean = home.getJsonData();

        if (bean.getCarousel().size() > 0) {
            list.add(new StoreMultipleItem<>(StoreMultipleItem.BOOK_BANNER, bean.getCarousel()));
        }
        if (bean.getHeadline().size() > 0) {
            list.add(new StoreMultipleItem<>(StoreMultipleItem.BOOK_NEWS, bean.getHeadline()));
        }

        list.add(new StoreMultipleItem<>(StoreMultipleItem.BOOK_RALLY, null));

        if (bean.getNearby_cases().size() > 0) {
            list.add(new StoreMultipleItem<>(StoreMultipleItem.BOOK_CASE, bean.getNearby_cases()));
        }
        if (bean.getMid_banner().size() > 0) {
            list.add(new StoreMultipleItem<>(StoreMultipleItem.BOOK_ADVERTISE, bean.getMid_banner()));
        }
        if (bean.getConcern_books().size() > 0) {
            list.add(new StoreMultipleItem<>(StoreMultipleItem.BOOK_CENTRE, bean.getConcern_books()));
        }

        if (bean.getSell_well_books().size() > 0) {
            list.add(new StoreMultipleItem<>(StoreMultipleItem.BOOK_SELLING, bean.getSell_well_books()));
        }

        if (bean.getRecommend_books().size() > 0) {
            list.add(new StoreMultipleItem<>(StoreMultipleItem.BOOK_TASTE, bean.getRecommend_books()));
        }
        multiItemAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onError(String error) {
        showToast(error);
    }

    @Override
    public void onEmpty() {

    }
}
