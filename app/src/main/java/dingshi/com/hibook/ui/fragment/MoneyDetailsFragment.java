package dingshi.com.hibook.ui.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.IMoneyDetails;
import dingshi.com.hibook.action.IRequestView;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.MoneyDetails;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.present.MoneyDetailsPresent;
import dingshi.com.hibook.utils.PixelUtils;
import dingshi.com.hibook.view.DividVerticalDecoration;
import dingshi.com.hibook.view.LoadingLayout;

/**
 * @author wangqi
 * @since 2017/11/7 上午10:59
 */

public class MoneyDetailsFragment extends BaseFragment implements IRequestView<MoneyDetails> {
    @BindView(R.id.money_details_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.money_details_recycle)
    RecyclerView mRecycleview;
    @BindView(R.id.money_details_smart)
    SmartRefreshLayout smartRefreshLayout;

    FuckYouAdapter fuckYouAdapter;

    MoneyDetailsPresent present = new MoneyDetailsPresent(this, this);


    List<MoneyDetails.JsonDataBean> list = new ArrayList<>();
    /**
     * 1.消费，2.充值，3.收入
     */
    int genre;

    /**
     * 分页加载
     */
    int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_money_details;
    }


    @Override
    public void initView() {
        genre = getArguments().getInt("genre");

        Log.i("---", "---------genre-----------" + genre);

        mRecycleview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecycleview.addItemDecoration(new DividVerticalDecoration(mActivity, PixelUtils.dip2Px(mActivity, 10), 0xfff1f1f1));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_money_details, list);
        mRecycleview.setAdapter(fuckYouAdapter);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                present.loadData(genre, page, user.getJsonData().getUser_id());
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                present.loadData(genre, page, user.getJsonData().getUser_id());
            }
        });


        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                MoneyDetails.JsonDataBean bean = (MoneyDetails.JsonDataBean) item;
                helper.setText(R.id.money_details_order, "订单号: " + bean.getOut_trade_no());
                helper.setText(R.id.money_details_time, bean.getCreated_at());
                helper.setText(R.id.money_detail_price, "￥" + bean.getAmount());
                helper.setText(R.id.money_detail_content, bean.getOrder_info().getCase_address());
//                helper.setText(R.id.money_detail_content, bean.getOrder_info().getCase_name());
                helper.setText(R.id.money_detail_borrow, "借书" + bean.getOrder_info().getBorrow_day() + "天");
            }
        });

    }


    @Override
    public void lazyLoad() {
        present.loadData(genre, page, user.getJsonData().getUser_id());
    }


    @Override
    public void onLoad() {
        loadingLayout.showLoadingView();
    }


    @Override
    public void onSuccess(MoneyDetails details) {
        loadingLayout.showContentView();
        if (page == 1) {
            list.clear();
        }
        list.addAll(details.getJsonData());
        fuckYouAdapter.notifyDataSetChanged();
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
