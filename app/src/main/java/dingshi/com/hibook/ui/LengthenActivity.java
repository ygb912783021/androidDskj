package dingshi.com.hibook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;

import org.w3c.dom.Text;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.present.PayBookPresent;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.view.QuantityView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/26 上午11:35
 * 延长时间
 */


public class LengthenActivity extends BaseActivity {
    @BindView(R.id.lengthen_img)
    ImageView imgPhoto;
    @BindView(R.id.lengthen_author)
    TextView txAuthor;
    @BindView(R.id.lengthen_book)
    TextView txName;
    @BindView(R.id.lengthen_concern)
    TextView txConcern;
    @BindView(R.id.lengthen_concern_time)
    TextView txConcernTime;
    @BindView(R.id.lengthen_quantity)
    QuantityView quantityView;
    @BindView(R.id.lengthen_price)
    TextView txPrice;

    /**
     * 图书isbn
     */
    private String isbn;
    /**
     * 订单号
     */
    private String order;
    /**
     *
     */
    private String serialNumber;

    BookDetails bookDetails;

    HashMap<String, String> map = new HashMap<>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_lengthen;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "延长时间");
//        isbn = getIntent().getStringExtra("isbn");
//        order = getIntent().getStringExtra("out_trade_no");
//        serialNumber = getIntent().getStringExtra("serial_number");
        map = (HashMap<String, String>) getIntent().getSerializableExtra("map");


        getBook();
        quantityView.setOnBackDay(new QuantityView.OnBackValue() {
            @Override
            public void day(int day) {
                if (day <= 10) {
                    txPrice.setText("合计: 1元");
                } else {
                    txPrice.setText("合计: " + ((day - 10) * 0.5 + 1) + "元");
                }
            }
        });
    }


    @OnClick({R.id.lengthen_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lengthen_pay:
                if (bookDetails == null) {
                    return;
                }
                map.put("borrow_day", String.valueOf(quantityView.getValue()));
                Intent intent = new Intent(this, PayBookActivity.class);
                intent.putExtra("bookDetails", bookDetails.getJsonData());
                intent.putExtra("map", map);
                startActivity(intent);
                finish();
                break;
            default:
        }
    }


    /**
     * 获取图书详情
     */
    public void getBook() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<BookDetails>("bookShow") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {

            }

            @Override
            protected void onSuccess(BookDetails response) {
                if (response.getJsonData() != null) {
                    LengthenActivity.this.bookDetails = response;
                    updateUI();
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", isbn);
        map = AppSign.buildMap(map);
        Observable<BookDetails> observable = NetUtils.getGsonRetrofit().bookShow(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);

    }


    /**
     * 更新ui
     */
    private void updateUI() {
        GlideUtils.load(this, bookDetails.getJsonData().getCover(), imgPhoto);
        txName.setText(bookDetails.getJsonData().getName());
        txAuthor.setText("作者: " + bookDetails.getJsonData().getAuthor());
        if (bookDetails!=null&&bookDetails.getJsonData()!=null&&bookDetails.getJsonData().getPress()!=null){
            if (bookDetails.getJsonData().getPress().contains("null")||
                    bookDetails.getJsonData().getPress()==""){
                txConcern.setText("出版社: 暂无");
            }else {
                txConcern.setText("出版社: " + bookDetails.getJsonData().getPress());
            }
        }
        txConcernTime.setText("出版时间: " + bookDetails.getJsonData().getPublish_time());
    }

}
