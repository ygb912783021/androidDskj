package dingshi.com.hibook.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import dingshi.com.hibook.action.IOrderView;
import dingshi.com.hibook.action.IRequestView;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.Order;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.present.OrderPresent;
import dingshi.com.hibook.ui.OrderDetailsActivity;
import dingshi.com.hibook.utils.SpacesItemDecoration;
import dingshi.com.hibook.view.LoadingLayout;

/**
 * @author wangqi
 * @since 2017/11/7 上午11:17
 */


public class OrderFragment extends BaseFragment implements IRequestView<Order> {

    @BindView(R.id.order_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.order_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.order_smart)
    SmartRefreshLayout smartRefreshLayout;

    FuckYouAdapter fuckYouAdapter;

    /**
     * 0.所有，1.已支付，2.未支付
     */
    int payment_status = 0;

    int page = 1;

    List<Order.JsonDataBean> list = new ArrayList<>();

    OrderPresent present = new OrderPresent(this, this);

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    public void initView() {
        payment_status = getArguments().getInt("payment_status");
        fuckYouAdapter = new FuckYouAdapter<Order.JsonDataBean>(R.layout.view_item_order, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SpacesItemDecoration(13));
        recyclerView.setAdapter(fuckYouAdapter);


        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                final Order.JsonDataBean bean = (Order.JsonDataBean) item;
                helper.setText(R.id.item_order_order, "订单号: " + bean.getOut_trade_no());
                helper.setText(R.id.item_order_timer, bean.getPay_at());
                helper.setText(R.id.item_order_book, bean.getBook().getName());
                helper.setText(R.id.item_order_borrow, "借书" + bean.getBorrow_day() + "天");

                TextView tip = helper.getView(R.id.item_order_tip);
                TextView submit = helper.getView(R.id.item_order_submit);
                TextView price = helper.getView(R.id.item_order_price);

                price.setText("￥ " + bean.getPay_fee());

                //Payment_status   0.未支付，1.已支付
                //Comment_status   0.未评论，1.已评论
                if (bean.getPayment_status() == 0) {
                    tip.setText("待付款");
                    submit.setText("去付款");
                    submit.setBackgroundResource(R.drawable.circle_gold);
                    submit.setTextColor(getResources().getColor(R.color.gold_d8));
                    tip.setTextColor(getResources().getColor(R.color.red));
                    price.setTextColor(getResources().getColor(R.color.black));
                } else if (bean.getPayment_status() == 1) {
                    if (bean.getComment_status() == 0) {
                        tip.setText("交易成功");
                        submit.setText("去评价");
                        submit.setBackgroundResource(R.drawable.circle_black);
                        submit.setTextColor(getResources().getColor(R.color.black));
                        tip.setTextColor(getResources().getColor(R.color.black));
                        price.setTextColor(getResources().getColor(R.color.black));

                    } else if (bean.getComment_status() == 1) {
                        tip.setText("交易成功");
                        submit.setText("已评论");
                        submit.setBackgroundResource(R.drawable.circle_gray);
                        submit.setTextColor(getResources().getColor(R.color.gray_e));
                        tip.setTextColor(getResources().getColor(R.color.gray_e));
                        price.setTextColor(getResources().getColor(R.color.gray_e));
                    }
                }
                //付款和评价的点击处理
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //去支付
                        if (bean.getPayment_status() == 0) {
                            present.pay(bean.getTrade_platform(), bean.getSign());
                        } //去评价
                        else if (bean.getPayment_status() == 1 && bean.getComment_status() == 0) {

                        } else {

                        }
                    }
                });

            }
        });

        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Order.JsonDataBean bean = list.get(position);
                Intent intent = new Intent(mActivity, OrderDetailsActivity.class);
                intent.putExtra("bean", bean);
                startActivity(intent);
            }
        });


        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                present.load(user.getJsonData().getUser_id(), payment_status, page);
            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                present.load(user.getJsonData().getUser_id(), payment_status, page);
            }
        });
    }


    @Override
    public void lazyLoad() {
        present.load(user.getJsonData().getUser_id(), payment_status, page);
    }


    @Override
    public void onLoad() {
        loadingLayout.showLoadingView();
    }

    @Override
    public void onSuccess(Order result) {
        if (page == 1) {
            list.clear();
        }
        list.addAll(result.getJsonData());
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
        fuckYouAdapter.notifyDataSetChanged();
        loadingLayout.showContentView();
    }

    @Override
    public void onError(String error) {
        showToast(error);
        loadingLayout.showErrorView();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
    }

    @Override
    public void onEmpty() {
        loadingLayout.showEmptyView();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
    }
}
