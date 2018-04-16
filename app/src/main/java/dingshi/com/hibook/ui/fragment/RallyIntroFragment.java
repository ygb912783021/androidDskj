package dingshi.com.hibook.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.card.CardDetails;
import dingshi.com.hibook.bean.lib.LibCreate;
import dingshi.com.hibook.bean.lib.LibIntro;
import dingshi.com.hibook.bean.lib.LibList;
import dingshi.com.hibook.eventbus.LibRefreshEvent;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.card.CardDetailsActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.SpUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2018/2/23 下午4:45
 */

public class RallyIntroFragment extends BaseFragment {

    @BindView(R.id.rally_intro_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.rally_intro_smart)
    SmartRefreshLayout smartRefreshLayout;

    TextView txTitle;
    TextView txContent;
    TextView txDescribe;
    TextView txTotal;


    int page = 1;
    String catalogId;
    FuckYouAdapter fuckYouAdapter;


    List<LibIntro.JsonDataBean> list = new ArrayList<>();
    LibList.JsonDataBean libBean;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_rally_intro;
    }

    @Override
    public void initView() {
        catalogId = getArguments().getString("catalog_id");

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_rally_intro, list);
        recyclerView.setAdapter(fuckYouAdapter);

        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                final LibIntro.JsonDataBean bean = (LibIntro.JsonDataBean) item;
                helper.setText(R.id.rally_intro_id, helper.getLayoutPosition() + ".");
                helper.setText(R.id.rally_intro_nick, bean.getName());


                ImageView photo = helper.getView(R.id.rally_intro_photo);
                GlideUtils.loadCircleImage(mActivity, bean.getAvatar(), photo);

                helper.setText(R.id.rally_intro_job, bean.getPosition());
                helper.setText(R.id.rally_intro_company, bean.getCompany());

                helper.setText(R.id.rally_intro_content, "资源优势/个人介绍\n" + bean.getIntroduce());

                if (bean.getIs_creator() == 1) {
                    helper.getView(R.id.rally_intro_flag).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.rally_intro_flag).setVisibility(View.GONE);
                }

                //交换名片
                helper.getView(R.id.rally_intro_switch).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sweepCard(bean.getUser_id());

                    }
                });
            }
        });

        /**
         * 点击跳转卡片详情
         */
        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
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
        EventBus.getDefault().register(this);
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
        this.libBean=libBean;
        txTitle.setText(libBean.getName());
        txContent.setText("图书数量  " + libBean.getBook_sum() + "   共享图书数量  " + libBean.getBook_share_sum());
        txDescribe.setText("目录描述 :" + libBean.getDescribe());
        txTotal.setText("人数 " + libBean.getUser_total() + "人 / (限制" + libBean.getUser_limit() + "人)");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (libBean!=null){
            setIntro(libBean);
        }
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
     * 交换名片申请
     * <p>
     * 对方的用户id
     *
     * @param toUserId
     */
    public void sweepCard(final String toUserId) {

        HttpRxObserver httpRxObserver = new HttpRxObserver<CardDetails>("cardApply") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                showToast(e.getMsg());
            }

            @Override
            protected void onSuccess(CardDetails response) {
                EMMessage message = EMMessage.createTxtSendMessage("交换名片啦", toUserId);//526741470357491713
                message.setAttribute("borrow", "6000");
                message.setAttribute("apply_id", response.getJsonData().getApply_id());
                message.setAttribute("nick", user.getJsonData().getNick_name());
                message.setAttribute("avatar", user.getJsonData().getAvatar());
                message.setAttribute("userid", response.getJsonData().getApply_id());


                Log.i("RallyInfo", response.getJsonData().getApply_id());


                EMClient.getInstance().chatManager().sendMessage(message);
                showToast("已提交交换名片申请");
            }
        };
        /**
         *   还需要设置两个图片
         */
        HashMap<String, String> map = new HashMap<>(2);
        map.put("uid", toUserId);
        map = AppSign.buildMap(map);
        Observable<CardDetails> observable = NetUtils.getGsonRetrofit().cardApply(map);
        HttpRxObservable.getObservable(observable, this, FragmentEvent.PAUSE).subscribe(httpRxObserver);


        Log.i("userid", SpUtils.getUser().getJsonData().getUser_id());
        Log.i("userid", toUserId);
    }

    @Subscribe
    public void refresh(LibRefreshEvent libRefreshEvent) {
        page = 1;
        pullNet();


        Log.i("tag", "LibRefreshEvent-----");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
