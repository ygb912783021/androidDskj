package dingshi.com.hibook.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.IRequestView;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.MoneyDetails;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.present.MoneyDetailsPresent;
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
 * @since 2017/12/21 下午2:43
 */

public class ChargeDetailsFragment extends BaseFragment implements IRequestView<MoneyDetails> {

    @BindView(R.id.charge_details_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.charge_details_recycle)
    RecyclerView mRecycleview;
    @BindView(R.id.charge_details_smart)
    SmartRefreshLayout smartRefreshLayout;

    FuckYouAdapter fuckYouAdapter;

    MoneyDetailsPresent present = new MoneyDetailsPresent(this, this);
    List<MoneyDetails.JsonDataBean> list = new ArrayList<>();
    /**
     * 1.消费，2.充值，3.退款进度
     */
    int genre;

    /**
     * 分页加载
     */
    int page = 1;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_charge_details;
    }

    @Override
    public void initView() {
        genre = getArguments().getInt("genre");
        mRecycleview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecycleview.addItemDecoration(new DividVerticalDecoration(mActivity, PixelUtils.dip2Px(mActivity, 10), 0xfff1f1f1));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_charge_details, list);
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
                //1充值  2收入
                if (genre == 2) {
                    helper.setText(R.id.item_charge_details_price, getPayMethod(bean.getChannel()) + bean.getAmount() + "元");
                    helper.setText(R.id.item_charge_details_content, "+" + bean.getAmount() + "元");
                } else if (genre == 3) {
                    helper.setText(R.id.item_charge_details_price, "收入" + bean.getAmount() + "元");
                }

                helper.setText(R.id.item_charge_details_time, bean.getCreated_at());
            }
        });
    }

    @Override
    public void lazyLoad() {
        present.loadData(genre, page, user.getJsonData().getUser_id());

    }


    public String getPayMethod(int method) {
        if (method == 1) {
            return "支付宝支付";
        } else if (method == 2) {
            return "微信支付";
        } else if (method == 3) {
            return "余额支付";
        } else {
            return "优惠券支付";
        }
    }


    @Override
    public void onLoad() {
        loadingLayout.showLoadingView();
    }

    @Override
    public void onSuccess(MoneyDetails result) {
        loadingLayout.showContentView();
        if (page == 1) {
            list.clear();
        }
        list.addAll(result.getJsonData());
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
