package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import dingshi.com.hibook.action.ICouponView;
import dingshi.com.hibook.action.IRequestView;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Coupon;
import dingshi.com.hibook.present.CouponPresent;
import dingshi.com.hibook.view.LoadingLayout;

/**
 * @author wangqi
 * @since 2017/11/14 上午8:55
 */

public class CouponActivity extends BaseActivity implements IRequestView<Coupon> {
    public static final int COUPON_REQUEST_CODE = 0x1129;

    @BindView(R.id.coupon_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.coupon_smart)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.coupon_recycle)
    RecyclerView recyclerView;

    FuckYouAdapter fuckYouAdapter;

    CouponPresent present = new CouponPresent(this, this);


    List<Coupon.JsonDataBean> list = new ArrayList<>();
    int page = 1;

    /**
     * 判断是支付获取优惠券的入口还是我的页面优惠券详情
     */
    boolean payCoupon;

    @Override
    public int getLayoutId() {
        return R.layout.activity_coupon;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "我的优惠券");
        Log.i("josn","测试log");

        payCoupon = getIntent().getBooleanExtra("payCoupon", false);


        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_item_coupon, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(fuckYouAdapter);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                list.clear();
                present.refresh(user, page);
            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                present.refresh(user, page);
            }
        });


        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                Coupon.JsonDataBean bean = (Coupon.JsonDataBean) item;
                helper.setText(R.id.item_coupon_title, bean.getName());
                helper.setText(R.id.item_coupon_describe, bean.getExplication());
                helper.setText(R.id.item_coupon_date, String.format("有效期 %s~%s", bean.getStart_time(), bean.getEnd_time()));

                ImageView imgPrice = helper.getView(R.id.item_coupon_price);

                switch (bean.getPrice()) {
                    case 1:
                        helper.getView(R.id.item_coupon_layout).setBackgroundResource(R.drawable.coupon_bg_1);
                        imgPrice.setImageResource(R.drawable.coupon_txt_1);
                        break;
                    case 2:
                        helper.getView(R.id.item_coupon_layout).setBackgroundResource(R.drawable.coupon_bg_2);
                        imgPrice.setImageResource(R.drawable.coupon_txt_2);
                        break;
                    case 5:
                        helper.getView(R.id.item_coupon_layout).setBackgroundResource(R.drawable.coupon_bg_5);
                        imgPrice.setImageResource(R.drawable.coupon_txt_5);
                        break;
                    case 10:
                        helper.getView(R.id.item_coupon_layout).setBackgroundResource(R.drawable.coupon_bg_10);
                        imgPrice.setImageResource(R.drawable.coupon_txt_10);
                        break;
                    default:
                }


            }
        });

        present.refresh(user, page);
        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (payCoupon) {
                    Intent intent = getIntent();
                    intent.putExtra("bean", list.get(position));
                    setResult(COUPON_REQUEST_CODE, intent);
                    finish();
                } else {
                    Intent intent = new Intent(CouponActivity.this, CouponDetailsActivity.class);
                    intent.putExtra("bean", list.get(position));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onLoad() {
        loadingLayout.showLoadingView();
        //
    }

    @Override
    public void onSuccess(Coupon coupon) {
        loadingLayout.showContentView();
        list.addAll(coupon.getJsonData());
        fuckYouAdapter.notifyDataSetChanged();
        smartRefreshLayout.finishLoadmore();
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void onError(String error) {
        loadingLayout.showErrorView();
    }

    @Override
    public void onEmpty() {
        smartRefreshLayout.finishLoadmore();
        smartRefreshLayout.finishRefresh();
        loadingLayout.showEmptyView();
    }
}
