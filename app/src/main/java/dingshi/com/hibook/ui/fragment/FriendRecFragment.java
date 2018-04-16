package dingshi.com.hibook.ui.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.BookList;
import dingshi.com.hibook.bean.CommList;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.BookDetailsActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.PixelUtils;
import dingshi.com.hibook.view.DividVerticalDecoration;
import dingshi.com.hibook.view.LoadingLayout;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 *         A simple {@link Fragment} subclass.
 */
public class FriendRecFragment extends BaseFragment {
    @BindView(R.id.friend_rec_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.friend_rec_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.friend_rec_smart)
    SmartRefreshLayout smartRefreshLayout;

    FuckYouAdapter fuckYouAdapter;

    int page = 1;

    List<CommList.JsonDataBean> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_friend_rec;
    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_friend_item, list);
        recyclerView.setAdapter(fuckYouAdapter);

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

        fuckYouAdapter.setEmptyView(R.layout.loading_empty, recyclerView);


        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                CommList.JsonDataBean bean = (CommList.JsonDataBean) item;

                helper.setText(R.id.friend_item_content, bean.getContent());
                helper.setText(R.id.friend_item_zan_txt, bean.getPraise_total() + "");
                helper.setText(R.id.friend_item_eval_txt, bean.getComment_total() + "");

                CommList.JsonDataBean.BookBean bookBean = bean.getBook();
                ImageView cover = helper.getView(R.id.friend_item_book_cover);
                GlideUtils.load(mActivity, bookBean.getCover(), cover);
                helper.setText(R.id.friend_item_book_name, bookBean.getName());
                helper.setText(R.id.friend_item_book_author, bookBean.getAuthor());
                AppCompatRatingBar ratingBar = helper.getView(R.id.friend_item_book_rating);
                ratingBar.setRating(bookBean.getGrade() / 2);
                helper.setText(R.id.friend_item_book_score, bookBean.getGrade() + "分");
            }
        });

        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity, BookDetailsActivity.class);
                intent.putExtra("isbn", list.get(position).getBook().getIsbn());
                startActivity(intent);
            }
        });
    }

    @Override
    public void lazyLoad() {
        super.lazyLoad();
        pullNet();
    }


    public void pullNet() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<CommList>("libBookList") {
            @Override
            protected void onStart(Disposable d) {
                loadingLayout.showLoadingView();
            }

            @Override
            protected void onError(ApiException e) {
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();
                loadingLayout.showErrorView();
            }

            @Override
            protected void onSuccess(CommList commList) {
                if (commList.getJsonData() != null) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(commList.getJsonData());
                    loadingLayout.showContentView();
                } else {
                    loadingLayout.showEmptyView();
                }
                fuckYouAdapter.notifyDataSetChanged();
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();

            }
        };

        HashMap<String, String> map = new HashMap<>();
        map.put("uid", user.getJsonData().getUser_id());
        map.put("type", "1");
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);
        Observable<CommList> user = NetUtils.getGsonRetrofit().commonPerson(map);
        HttpRxObservable.getObservable(user, this, FragmentEvent.PAUSE).subscribe(httpRxObserver);

    }


}
