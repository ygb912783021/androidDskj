package dingshi.com.hibook.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.lib.LibIntro;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.KefuUtils;
import dingshi.com.hibook.view.LoadingLayout;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2018/2/24 下午4:55
 */

public class AddressBookActivity extends BaseActivity {
    @BindView(R.id.address_book_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.address_book_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.address_book_smart)
    SmartRefreshLayout smartRefreshLayout;

    FuckYouAdapter fuckYouAdapter;

    int page = 1;

    List<LibIntro.JsonDataBean> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_book;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "通讯录");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_rally_intro, list);
        recyclerView.setAdapter(fuckYouAdapter);


        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                final LibIntro.JsonDataBean bean = (LibIntro.JsonDataBean) item;
                helper.setText(R.id.rally_intro_id, (helper.getLayoutPosition() + 1) + ".");
                helper.setText(R.id.rally_intro_nick, bean.getName());


                ImageView photo = helper.getView(R.id.rally_intro_photo);
                GlideUtils.loadCircleImage(AddressBookActivity.this, bean.getAvatar(), photo);

                helper.setText(R.id.rally_intro_job, bean.getPosition());
                helper.setText(R.id.rally_intro_company, bean.getCompany());
                helper.setText(R.id.rally_intro_content, "资源优势/个人介绍\n" + bean.getIntroduce());

                if (bean.getIs_creator() == 1) {
                    helper.getView(R.id.rally_intro_flag).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.rally_intro_flag).setVisibility(View.GONE);
                }
                helper.setImageResource(R.id.rally_intro_switch, R.drawable.address_book_phone);
                //打电话
                helper.getView(R.id.rally_intro_switch).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        KefuUtils.callPhone(getApplication(), bean.getPhone());
                    }
                });
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

    /**
     * 获取通讯录列表
     */
    public void pullNet() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<LibIntro>("LibIntro") {
            @Override
            protected void onStart(Disposable d) {
                loadingLayout.showLoadingView();
            }

            @Override
            protected void onError(ApiException e) {
                loadingLayout.showErrorView();
            }

            @Override
            protected void onSuccess(LibIntro libIntro) {
                if (libIntro.getJsonData() != null) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(libIntro.getJsonData());
                    fuckYouAdapter.notifyDataSetChanged();
                    loadingLayout.showContentView();
                } else {
                    loadingLayout.showEmptyView();
                }
                smartRefreshLayout.finishLoadmore();
                smartRefreshLayout.finishRefresh();
            }
        };

        HashMap<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);

        Observable<LibIntro> user = NetUtils.getGsonRetrofit().addressBook(map);
        HttpRxObservable.getObservable(user, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

}
