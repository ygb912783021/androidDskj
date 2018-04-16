package dingshi.com.hibook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.MyApplicationLike;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.Order;
import dingshi.com.hibook.bean.OrderDetails;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppManager;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2018/1/2 下午6:01
 */


public class OrderBookActivity extends BaseActivity {
    /**
     * 图书信息
     */
    BookDetails.JsonDataBean bookDetails;
    /**
     * 订单号
     */
    String orderId;

    @BindView(R.id.order_book_order)
    TextView txOrderId;
    @BindView(R.id.order_book_id)
    TextView txCaseId;
    @BindView(R.id.order_book_img)
    ImageView imgPhoto;
    @BindView(R.id.order_book_name)
    TextView txBookName;
    @BindView(R.id.order_book_author)
    TextView txBookAuthor;
    @BindView(R.id.order_book_concern)
    TextView txConcern;
    @BindView(R.id.order_book_concern_time)
    TextView txConcernTime;
    @BindView(R.id.order_book_address)
    TextView txAddress;

    @BindView(R.id.order_book_submit)
    TextView txSubmit;
    /**
     * 判断是否是还书详情
     */
    boolean backBook;


    @Override
    public int getLayoutId() {
        return R.layout.activity_order_book;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "详情");
        orderId = getIntent().getStringExtra("orderId");
        backBook = getIntent().getBooleanExtra("backBook", false);

        if (backBook) {
            txSubmit.setText("还书");
        } else {
            txSubmit.setText("取书");
        }

        txOrderId.setText(orderId);
        queryOrder();
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

    Order.JsonDataBean jsonData;

    public void updateUI(Order.JsonDataBean jsonData) {
        this.jsonData = jsonData;
        GlideUtils.load(this, jsonData.getBook().getCover(), imgPhoto);
        txBookName.setText(jsonData.getBook().getName());
        txBookAuthor.setText("作者: " + jsonData.getBook().getAuthor());
        if (jsonData != null && jsonData.getBook() != null && jsonData.getBook().getPress() != null) {
            if (jsonData.getBook().getPress().contains("null") ||
                    jsonData.getBook().getPress() == "") {
                txConcern.setText("出版社: 暂无");

            } else {
                txConcern.setText("出版社: " + jsonData.getBook().getPress());
            }

        }
        txConcernTime.setText("出版时间: " + jsonData.getBook().getPublish_time());
        txAddress.setText(jsonData.getBookcase_address());
        txCaseId.setText(jsonData.getSerial_number());
    }

    @OnClick({R.id.order_book_submit})
    public void onClick(View view) {
        if (jsonData != null) {
            Intent intent = new Intent(this, LoadingActivity.class);
            HashMap<String, String> map = new HashMap<>();
            map.put("isbn", jsonData.getBook().getIsbn());
            map.put("lat", MyApplicationLike.lat);
            map.put("lng", MyApplicationLike.lng);
            map.put("serial_number", jsonData.getSerial_number());
            map.put("out_trade_no", orderId);
            if (backBook) {
                intent.putExtra("type", 3);
            } else {
                intent.putExtra("type", 1);
            }
            intent.putExtra("map", map);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishPage();
    }


    @Override
    public void onLeftClick() {
        super.onLeftClick();
        finishPage();
    }

    /**
     * 关闭页面逻辑处理
     */
    public void finishPage() {
        if (!backBook) {
            AppManager.getInstance().finishOthersActivity(MainActivity.class);
            Intent intent = new Intent(OrderBookActivity.this, MyBorrowActivity.class);
            startActivity(intent);
        }
    }
}
