package dingshi.com.hibook.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.lib.LibList;
import dingshi.com.hibook.eventbus.LibDeleteEvent;
import dingshi.com.hibook.eventbus.LibRefreshEvent;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.RallyDetailsActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/19 上午11:20
 */


public class LibActivity extends BaseActivity {
    @BindView(R.id.lib_smart)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.lib_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.lib_create)
    TextView txCreate;
    FuckYouAdapter fuckYouAdapter;
    List<LibList.JsonDataBean> list = new ArrayList<>();
    int page = 1;

    int currentPosition;
    /**
     * 是否是书友会(true)还是图书馆(false)
     */
    boolean isRally;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lib;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        isRally = getIntent().getBooleanExtra("isRally", false);

        if (isRally) {
            requestActionBarStyle(true, "我的书友会");
            txCreate.setText("创建书友会");
        } else {
            requestActionBarStyle(true, "我的图书馆");
            txCreate.setText("创建图书馆");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_item_lib, list);
        recyclerView.setAdapter(fuckYouAdapter);


        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currentPosition = position;
                Intent intent = new Intent();
                if (isRally) {
                    intent.putExtra("its_me", true);
                    intent.putExtra("pic", list.get(position).getIcon());
                    intent.setClass(LibActivity.this, RallyDetailsActivity.class);
                } else {
                    intent.setClass(LibActivity.this, LibDetailsActivity.class);
                }
                intent.putExtra("catalog_id", list.get(position).getCatalog_id());
                startActivity(intent);
            }
        });


        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                LibList.JsonDataBean bean = (LibList.JsonDataBean) item;
                helper.setText(R.id.item_lib_nick, bean.getName());
                helper.setText(R.id.item_lib_join, bean.getUser_total() + "人加入");
                helper.setText(R.id.item_lib_time, bean.getCreated_at());
                ImageView photo = helper.getView(R.id.item_lib_photo);
                GlideUtils.loadCircleImage(LibActivity.this, bean.getIcon(), photo);
            }
        });


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
        pullNet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pullNet();
    }

    @OnClick(R.id.lib_create)
    public void onClick(View v) {
        Intent intent = new Intent(this, CreateLibActivity.class);
        intent.putExtra("isRally", isRally);
        startActivityForResult(intent, CreateLibActivity.LIB_DATA_REQUEST);
    }


    /**
     * 获取图书馆列表
     */
    public void pullNet() {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("is_club", isRally ? "1" : "0");
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);

        HttpRxObserver httpRxObserver = new HttpRxObserver<LibList>("libList") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(LibList result) {
                if (result.getJsonData() != null) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(result.getJsonData());
                } else {
                    list.clear();
                }
                fuckYouAdapter.notifyDataSetChanged();
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();
            }
        };
        Observable<LibList> observable = NetUtils.getGsonRetrofit().libList(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (CreateLibActivity.LIB_DATA_REQUEST == requestCode) {
            LibList.JsonDataBean bean = (LibList.JsonDataBean) data.getSerializableExtra("bean");
            list.add(0, bean);
            fuckYouAdapter.notifyDataSetChanged();
        }
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
        pullNet();
    }

    /**
     * 刷新图书馆
     */
    @Subscribe
    public void libRefresh(LibRefreshEvent libRefreshEvent) {
        pullNet();
    }


}
