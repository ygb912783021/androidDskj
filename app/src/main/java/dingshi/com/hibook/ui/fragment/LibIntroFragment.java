package dingshi.com.hibook.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.bean.lib.LibCreate;
import dingshi.com.hibook.bean.lib.LibIntro;
import dingshi.com.hibook.bean.lib.LibList;
import dingshi.com.hibook.eventbus.EventBusHelper;
import dingshi.com.hibook.eventbus.LibRefreshEvent;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.card.CardDetailsActivity;
import dingshi.com.hibook.ui.card.MyCardActivity;
import dingshi.com.hibook.ui.library.JoinLibActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.SpUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Part;

/**
 * @author wangqi
 * @since 2017/12/20 下午4:28
 * <p>
 * <p>
 * 图书馆介绍
 */


public class LibIntroFragment extends BaseFragment {
    @BindView(R.id.lib_intro_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.lib_intro_smart)
    SmartRefreshLayout smartRefreshLayout;

    TextView txTitle;
    TextView txContent;
    TextView txDescribe;
    TextView txTotal;


    int page = 1;
    int shareNum=0;
    String catalogId;
    FuckYouAdapter fuckYouAdapter;


    List<LibIntro.JsonDataBean> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_lib_intro;
    }

    @Override
    public void initView() {
        catalogId = getArguments().getString("catalog_id");


        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_lib_user, list);
        recyclerView.setAdapter(fuckYouAdapter);

        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                LibIntro.JsonDataBean bean = (LibIntro.JsonDataBean) item;
                helper.setText(R.id.view_lib_user_id, helper.getLayoutPosition() + ".");
                helper.setText(R.id.view_lib_user_nick, bean.getName());
                ImageView photo = helper.getView(R.id.view_lib_user_photo);
                GlideUtils.loadCircleImage(mActivity, bean.getAvatar(), photo);
                helper.setText(R.id.view_lib_user_content, "藏书 :" + bean.getTotal() + "   共享图书 :" + bean.getShare());
                shareNum=bean.getShare();

                if (bean.getIs_creator() == 1) {
                    helper.getView(R.id.view_lib_user_flag).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.view_lib_user_flag).setVisibility(View.GONE);
                }
            }
        });

        /**
         * 点击跳转卡片详情
         */
        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (TextUtils.isEmpty(list.get(position).getUser_id())) {
                    return;
                }
                Intent intent = new Intent(mActivity, CardDetailsActivity.class);
                intent.putExtra("card_id", list.get(position).getCard_id());
                intent.putExtra("uid", list.get(position).getUser_id());
                startActivity(intent);
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

        initHeader();
    }

    @Override
    public void lazyLoad() {
        super.lazyLoad();
        pullNet();
//        EventBus.getDefault().register(this);
        EventBusHelper.register(this);
    }


    public void initHeader() {
        View header = LayoutInflater.from(mActivity).inflate(R.layout.lib_intro_header, null, false);
        txTitle = header.findViewById(R.id.lib_intro_title);
        txContent = header.findViewById(R.id.lib_intro_content);
        txDescribe = header.findViewById(R.id.lib_intro_describe);
        txTotal = header.findViewById(R.id.lib_intro_total);
        fuckYouAdapter.addHeaderView(header);
    }


    /**
     * 设置图书介绍
     */
    public void setIntro(LibList.JsonDataBean libBean) {
        txTitle.setText(libBean.getName());
//        txContent.setText("图书数量  " + libBean.getBook_sum() + "   共享图书数量  " + libBean.getBook_share_sum());
        txContent.setText("图书数量  " + libBean.getBook_sum() + "   共享图书数量  " + shareNum);
        txDescribe.setText("图书描述 :" + libBean.getDescribe());
        txTotal.setText("人数 " + libBean.getUser_total() + "人 / (限制" + libBean.getUser_limit() + "人)");
    }


    /**
     * 获取用户列表
     */
    public void pullNet() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<LibIntro>("LibIntro") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(LibIntro libIntro) {
                if (libIntro.getJsonData() != null) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(libIntro.getJsonData());
                    fuckYouAdapter.notifyDataSetChanged();
                }
                smartRefreshLayout.finishLoadmore();
                smartRefreshLayout.finishRefresh();
            }
        };

        HashMap<String, String> map = new HashMap<>();
        map.put("catalog_id", catalogId);
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);

        Observable<LibIntro> user = NetUtils.getGsonRetrofit().libUser(map);
        HttpRxObservable.getObservable(user, this, FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }


    /**
     * 刷新图书馆
     */
    @Subscribe
    public void libRefresh(LibRefreshEvent libRefreshEvent) {
        pullNet();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unRegister(this);
    }
}
