package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.IBookListView;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.BookList;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.present.BookSearchPresent;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.PixelUtils;
import dingshi.com.hibook.view.DividVerticalDecoration;
import dingshi.com.hibook.view.LoadingLayout;
import retrofit2.http.Part;

/**
 * @author wangqi
 * @since 2017/12/15 下午5:20
 */

public class BookListActivity extends BaseActivity implements IBookListView {
    @BindView(R.id.book_list_smart)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.book_list_recycle)
    RecyclerView mRecycleview;
    @BindView(R.id.book_list_loading)
    LoadingLayout loadingLayout;

    FuckYouAdapter fuckYouAdapter;

    List<BookDetails.JsonDataBean> list = new ArrayList<>();


    HashMap<String, String> map;
    int page = 1;

    BookSearchPresent present = new BookSearchPresent(this, this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_list;
    }

    /**
     * 如果是搜索请求
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "图书列表", "");
        map = (HashMap<String, String>) getIntent().getSerializableExtra("map");
        present.search(map, String.valueOf(page));

        mRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycleview.addItemDecoration(new DividVerticalDecoration(this, PixelUtils.dip2Px(this, 10), 0xfff1f1f1));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_list_item, list);
        mRecycleview.setAdapter(fuckYouAdapter);

        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                BookDetails.JsonDataBean bean = (BookDetails.JsonDataBean) item;
                ImageView bookPhoto = helper.getView(R.id.item_list_photo);
                GlideUtils.load(BookListActivity.this, bean.getCover(), bookPhoto);
                helper.setText(R.id.item_list_book, bean.getName());
                helper.setText(R.id.item_list_author, "作者: " + bean.getAuthor());
                if (bean != null && bean.getPress() != null) {
                    if (bean.getPress().contains("null") || bean.getPress() == "") {
                        helper.setText(R.id.item_list_concern, "出版社: 暂无");
                    } else {
                        helper.setText(R.id.item_list_concern, "出版社: " + bean.getPress());
                    }
                }
                helper.setText(R.id.item_list_concern_time, "出版时间: " + bean.getPublish_time());


            }
        });

        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(BookListActivity.this, BookDetailsActivity.class);
                intent.putExtra("isbn", list.get(position).getIsbn());
                startActivity(intent);
            }
        });

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                present.search(map, String.valueOf(page));
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                present.search(map, String.valueOf(page));
            }
        });

    }

    /**
     * 搜索成功数据返回
     *
     * @param bookList
     */
    @Override
    public void onSearch(BookList bookList) {
        if (page == 1) {
            list.clear();
        }

        View view = LayoutInflater.from(this).inflate(R.layout.search_empty, null, false);
        fuckYouAdapter.addHeaderView(view);

        list.addAll(bookList.getJsonData());
        fuckYouAdapter.notifyDataSetChanged();
        loadingLayout.showContentView();
        smartRefreshLayout.setEnableLoadmore(false);
        smartRefreshLayout.setEnableRefresh(false);
    }

    @Override
    public void onLoad() {
        loadingLayout.showLoadingView();
    }

    /**
     * 请求成功的返回
     *
     * @param bookList
     */
    @Override
    public void onSuccess(BookList bookList) {
        if (page == 1) {
            list.clear();
        }
        list.addAll(bookList.getJsonData());
        fuckYouAdapter.notifyDataSetChanged();
        loadingLayout.showContentView();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
    }

    @Override
    public void onError(String error) {
        loadingLayout.showErrorView();
    }

    @Override
    public void onEmpty() {
        loadingLayout.showEmptyView();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
    }
}
