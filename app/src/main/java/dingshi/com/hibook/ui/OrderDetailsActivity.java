package dingshi.com.hibook.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookList;
import dingshi.com.hibook.bean.Order;
import dingshi.com.hibook.bean.OrderDetails;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.view.TitleView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/11/14 下午5:41
 */


public class OrderDetailsActivity extends BaseActivity {

    @BindView(R.id.order_details_order)
    TextView txOrder;
    @BindView(R.id.order_details_id)
    TextView txId;
    @BindView(R.id.order_details_book)
    TextView txBook;
    @BindView(R.id.order_details_author)
    TextView txAuthor;
    @BindView(R.id.order_details_concern)
    TextView txConcern;
    @BindView(R.id.order_details_concern_time)
    TextView txConcernTime;
    @BindView(R.id.order_details_address)
    TextView txAddress;
    @BindView(R.id.order_details_rule)
    TextView txRule;
    @BindView(R.id.order_details_time)
    TextView txTime;
    @BindView(R.id.order_details_price)
    TextView txPrice;
    @BindView(R.id.order_details_pay)
    TextView txPay;
    @BindView(R.id.order_details_img)
    ImageView bookImg;


    Order.JsonDataBean bean;

    String orderId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "订单详情", "");

        bean = (Order.JsonDataBean) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            updateUI(bean);
        } else {
            orderId = getIntent().getStringExtra("out_trade_no");
            queryOrder();
        }
    }


    @OnClick({R.id.order_details_submit})
    public void onClick(View v) {
    }

    /**
     * 查看订单详情
     */
    public void queryOrder() {

        HttpRxObserver httpRxObserver = new HttpRxObserver<OrderDetails>("orderShow") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(OrderDetails orderDetails) {
                if (orderDetails.getJsonData() != null) {
                    updateUI(orderDetails.getJsonData());
                }
            }
        };
        HashMap<String, String> map = new HashMap<>();
        map.put("out_trade_no", orderId);
        map = AppSign.buildMap(map);
        Observable<OrderDetails> observable = NetUtils.getGsonRetrofit().orderShow(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 更新ui
     */
    private void updateUI(Order.JsonDataBean bean) {
        txOrder.setText(bean.getOut_trade_no());
        txId.setText(bean.getBook().getIsbn());
        txBook.setText(bean.getBook().getName());
        txAuthor.setText(bean.getBook().getAuthor());
        txConcern.setText(bean.getBook().getPress());
        txTime.setText(bean.getPay_at());
        txConcernTime.setText(bean.getBook().getPublish_time());
        txAddress.setText(bean.getBookcase_address());

        txPrice.setText(bean.getBook().getPrice());
        /*
            1.支付宝,2.微信,3.钱包
         */
        switch (bean.getTrade_platform()) {
            case 1:
                txPay.setText("支付宝");
                break;
            case 2:
                txPay.setText("微信");
                break;
            case 3:
                txPay.setText("钱包");
                break;
            default:
                txPay.setText("未知");

        }

        GlideUtils.load(this, bean.getBook().getCover(), bookImg);
    }
}
