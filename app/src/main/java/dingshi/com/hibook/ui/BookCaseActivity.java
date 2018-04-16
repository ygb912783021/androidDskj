package dingshi.com.hibook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.MyApplicationLike;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookCase;
import dingshi.com.hibook.bean.BookTalent;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.PixelUtils;
import dingshi.com.hibook.view.DividVerticalDecoration;
import dingshi.com.hibook.view.LoadingLayout;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/28 下午1:57
 */

public class BookCaseActivity extends BaseActivity {

    @BindView(R.id.book_case_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.book_case_smart)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.book_case_recycle)
    RecyclerView mRecycleview;
    FuckYouAdapter fuckYouAdapter;

    List<BookCase.JsonDataBean> list = new ArrayList<>();
    int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_case;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "附近书柜");
        mRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.item_book_case, list);
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
                BookCase.JsonDataBean bean = (BookCase.JsonDataBean) item;

                helper.setText(R.id.item_case_address, bean.getName());
                helper.setText(R.id.item_case_distance, bean.getRadius() + "km");
                helper.setText(R.id.item_case_content, "目前藏书" + bean.getBook_num() + "本,已预约" + bean.getSub_num() + "本");

            }
        });

        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(BookCaseActivity.this, Case2BookActivity.class);
                intent.putExtra("radius", list.get(position).getRadius());
                intent.putExtra("serial_number", list.get(position).getSerial_number());
                startActivity(intent);
            }
        });
        pullNet();
    }


    public void pullNet() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<BookCase>("bookCaseList") {
            @Override
            protected void onStart(Disposable d) {
                loadingLayout.showLoadingView();
            }

            @Override
            protected void onError(ApiException e) {
                loadingLayout.showErrorView();
            }

            @Override
            protected void onSuccess(BookCase response) {
                if (response.getJsonData() != null) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(response.getJsonData());
                    fuckYouAdapter.notifyDataSetChanged();
                    loadingLayout.showContentView();
                } else {
                    loadingLayout.showEmptyView();
                }
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();
            }
        };


        HashMap<String, String> map = new HashMap<>(2);
        map.put("lat", MyApplicationLike.lat);
        map.put("lng", MyApplicationLike.lng);
        map = AppSign.buildMap(map);
        Observable<BookCase> observable = NetUtils.getGsonRetrofit().bookCaseList(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


}
