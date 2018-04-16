package dingshi.com.hibook.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.IBookListView;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.BookList;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.present.BookListPresent;
import dingshi.com.hibook.ui.BookDetailsActivity;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.PixelUtils;
import dingshi.com.hibook.view.DividVerticalDecoration;
import dingshi.com.hibook.view.LoadingLayout;

/**
 * @author wangqi
 */
public class BookListFragment extends BaseFragment implements IBookListView {
    @BindView(R.id.book_list_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.book_list_recycle)
    RecyclerView mRecycleview;
    @BindView(R.id.book_list_smart)
    SmartRefreshLayout smartRefreshLayout;
    FuckYouAdapter fuckYouAdapter;


    int page = 1;
    String catalogId;

    List<BookDetails.JsonDataBean> list = new ArrayList<>();

    BookListPresent present = new BookListPresent(this, this);

    @Override
    public int getLayoutId() {
        return R.layout.fragment_book_list;
    }

    @Override
    public void initView() {

        mRecycleview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecycleview.addItemDecoration(new DividVerticalDecoration(mActivity, PixelUtils.dip2Px(mActivity, 10), 0xfff1f1f1));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_list_item, list);
        mRecycleview.setAdapter(fuckYouAdapter);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                pullNet();
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                pullNet();
            }
        });

        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                BookDetails.JsonDataBean bean = (BookDetails.JsonDataBean) item;
                ImageView bookPhoto = helper.getView(R.id.item_list_photo);
                GlideUtils.load(mActivity, bean.getCover(), bookPhoto);
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
                Intent intent = new Intent(mActivity, BookDetailsActivity.class);
                intent.putExtra("isbn", list.get(position).getIsbn());
                startActivity(intent);
            }
        });
    }


    public void pullNet() {
        if (getArguments().getString("catalog_id") != null) {
            catalogId = getArguments().getString("catalog_id");
            present.myLibBookList(catalogId, String.valueOf(page));
        }
    }


    @Override
    public void lazyLoad() {
        super.lazyLoad();
        pullNet();
    }


    @Override
    public void onLoad() {
        loadingLayout.showLoadingView();
    }

    @Override
    public void onSuccess(BookList result) {

    }

    @Override
    public void onError(String error) {
        loadingLayout.showErrorView();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
    }

    @Override
    public void onEmpty() {
        loadingLayout.showEmptyView();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
    }

    @Override
    public void onSearch(BookList bookList) {
        if (page == 1) {
            list.clear();
        }
        list.addAll(bookList.getJsonData());
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
        fuckYouAdapter.notifyDataSetChanged();
        loadingLayout.showContentView();
    }
}
