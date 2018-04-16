package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yuyh.library.imgsel.bean.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.RallySearch;
import dingshi.com.hibook.bean.lib.ClubList;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2018/2/1 下午1:54
 */

public class RallySearchActivity extends BaseActivity {

    @BindView(R.id.rally_search_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.rally_search_content)
    EditText editSearch;
    @BindView(R.id.rally_search_smart)
    SmartRefreshLayout smartRefreshLayout;


    FuckYouAdapter fuckYouAdapter;

    String keyWords = "";

    int page = 1;

    int type = 0;


    List<ClubList.JsonDataBean.DataBean> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_rally_search;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "官方书友会");
        type = getIntent().getIntExtra("type", 0);
        if (type != 0) {
            getRallyList();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_item_rally_chidren, list);
        recyclerView.setAdapter(fuckYouAdapter);


        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                ClubList.JsonDataBean.DataBean bean = (ClubList.JsonDataBean.DataBean) item;

                helper.setText(R.id.item_rally_child_title, bean.getName());
                ImageView avatar = helper.getView(R.id.item_rally_child_photo);
                GlideUtils.loadCircleImage(RallySearchActivity.this, bean.getIcon(), R.drawable.logo, avatar);
                helper.setText(R.id.item_rally_child_authen, bean.getPerson_num() + "人加入");
                helper.setText(R.id.item_rally_child_time, bean.getCreated_at());

            }
        });
        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.setClass(RallySearchActivity.this, RallyDetailsActivity.class);
                intent.putExtra("catalog_id", list.get(position).getCatalog_id());
                intent.putExtra("pic", list.get(position).getIcon());
                startActivity(intent);
            }
        });


        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                if (type > 0) {
                    getRallyList();
                } else {
                    pullNet();
                }

            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                if (type > 0) {
                    getRallyList();
                } else {
                    pullNet();
                }
            }
        });


        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (handler.hasMessages(MSG_SEARCH)) {
                    handler.removeMessages(MSG_SEARCH);
                }
                handler.sendEmptyMessageDelayed(MSG_SEARCH, 500);

            }
        });
    }


    public static int MSG_SEARCH = 0x01;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            keyWords = editSearch.getText().toString();
            page = 1;
            pullNet();
        }
    };


    private void pullNet() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<RallySearch>("clubSearch") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();
            }

            @Override
            protected void onSuccess(RallySearch response) {
                if (response.getJsonData() != null) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(response.getJsonData());
                    fuckYouAdapter.notifyDataSetChanged();
                } else {
                    showToast("没搜索到");
                }
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();

            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("keyword", keyWords);
//        map.put("type", String.valueOf(type));
//        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);

        Observable<RallySearch> observable = NetUtils.getGsonRetrofit().clubSearch(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    public void getRallyList() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<RallySearch>("clubSearch") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();
            }

            @Override
            protected void onSuccess(RallySearch response) {
                if (response.getJsonData() != null) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(response.getJsonData());
                    fuckYouAdapter.notifyDataSetChanged();
                } else {
                    showToast("没搜索到");
                }
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();

            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("type", String.valueOf(type));
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);

        Observable<RallySearch> observable = NetUtils.getGsonRetrofit().clubLists(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
}
