package dingshi.com.hibook.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.adapter.NoticeMultiAdapter;
import dingshi.com.hibook.adapter.NoticeMultipleItem;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.CommList;
import dingshi.com.hibook.bean.Notice;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.BookDetailsActivity;
import dingshi.com.hibook.ui.Case2BookActivity;
import dingshi.com.hibook.ui.WebActivity;
import dingshi.com.hibook.ui.card.CardDetailsActivity;
import dingshi.com.hibook.ui.library.JoinLibActivity;
import dingshi.com.hibook.ui.library.LibDetailsActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.view.LoadingLayout;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Part;

/**
 * @author wangqi
 * @since 2017/12/21 上午11:49
 */


public class MsgNoticeFragment extends BaseFragment {
    @BindView(R.id.msg_notice_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.msg_notice_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.msg_notice_smart)
    SmartRefreshLayout smartRefreshLayout;

    NoticeMultiAdapter fuckYouAdapter;

    List<NoticeMultipleItem> list = new ArrayList<>();

    int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_msg_notice;
    }

    @Override
    public void initView() {
        fuckYouAdapter = new NoticeMultiAdapter(mActivity, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(fuckYouAdapter);

        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                jump((Notice.JsonDataBean) list.get(position).getData());
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
    }

    /**
     * 根据type来跳转不同的view
     * 1.图书，2.书柜，3.图书馆，4.个人名片，5.个人中心，6.文本信息,7.打开外部页面
     *
     * @param bean
     */
    public void jump(Notice.JsonDataBean bean) {
        Intent intent = new Intent();
        switch (bean.getType()) {
            case 1:
                intent.setClass(mActivity, BookDetailsActivity.class);
                intent.putExtra("isbn", bean.getSource_id());
                startActivity(intent);
                break;
            case 2:
                intent.setClass(mActivity, Case2BookActivity.class);
                intent.putExtra("serial_number", bean.getSource_id());
                intent.putExtra("radius", "1000");
                startActivity(intent);
                break;
            case 3:
                intent.setClass(mActivity, JoinLibActivity.class);
                intent.putExtra("catalog_id", bean.getSource_id());
                startActivity(intent);
                break;
            case 4:
                intent.setClass(mActivity, CardDetailsActivity.class);
                intent.putExtra("uid", bean.getSource_id());
                intent.putExtra("card_id", "");
                startActivity(intent);
                break;
            case 5:
                MainActivity activity = (MainActivity) mActivity;
                activity.setTabSelection(5);
                break;
            case 6:
                break;
            case 7:
                intent.setClass(mActivity, WebActivity.class);
                intent.putExtra("url", bean.getSource_url());
                startActivity(intent);
                break;
            default:
        }
    }


    @Override
    public void lazyLoad() {
        super.lazyLoad();
        pullNet();
    }

    public void pullNet() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Notice>("showNotice") {
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
            protected void onSuccess(Notice notice) {
                if (notice.getJsonData() != null) {
                    loadingLayout.showContentView();
                    if (page == 1) {
                        list.clear();
                    }
                    addData(notice.getJsonData());
                } else {
                    loadingLayout.showEmptyView();
                }
                fuckYouAdapter.notifyDataSetChanged();
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();


            }
        };
        HashMap<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);
        Observable<Notice> observable = NetUtils.getGsonRetrofit().showNotice(map);
        HttpRxObservable.getObservable(observable, this, FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }

    public void addData(List<Notice.JsonDataBean> jsonData) {
        for (Notice.JsonDataBean bean : jsonData) {
            //如果是文本消息，则需要另一个布局
            if (bean.getType() == 6) {
                list.add(new NoticeMultipleItem(NoticeMultipleItem.NOTICE_TXT, bean));
            } else {
                list.add(new NoticeMultipleItem(NoticeMultipleItem.NOTICE_MSG, bean));
            }
        }
    }
}
