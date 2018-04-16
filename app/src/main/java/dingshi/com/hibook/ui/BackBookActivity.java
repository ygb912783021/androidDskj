package dingshi.com.hibook.ui;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.ActivityEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.MyApplicationLike;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookCase;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.Borrows;
import dingshi.com.hibook.bean.Order;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.fragment.BackBookFragment;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
/**
 * @author wangqi
 * @since 2017/12/26 上午10:52
 */
public class BackBookActivity extends BaseActivity {
    @BindView(R.id.back_book_case)
    RecyclerView recyclerView;
    @BindView(R.id.back_book_img)
    ImageView imgPhoto;
    @BindView(R.id.back_book_name)
    TextView txName;
    @BindView(R.id.back_book_author)
    TextView txAuthor;
    @BindView(R.id.back_book_concern)
    TextView txConcern;
    @BindView(R.id.back_book_concern_time)
    TextView txConcernTime;
    @BindView(R.id.back_book_pay)
    TextView txSubmit;
    FuckYouAdapter fuckYouAdapter;
    List<BookCase.JsonDataBean> list = new ArrayList<>();
    int current = 0;
    /**
     * 图书isbn
     */
    private String isbn;
    /**
     * 订单号
     */
    private String order;
    BookDetails bookDetails;
    @Override
    public int getLayoutId() {
        return R.layout.activity_back_book;
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "选择书柜", "");
        isbn = getIntent().getStringExtra("isbn");
        order = getIntent().getStringExtra("out_trade_no");
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_book_details_case_item, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(fuckYouAdapter);
        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                BookCase.JsonDataBean bean = (BookCase.JsonDataBean) item;
                if (current == helper.getLayoutPosition()) {
                    helper.getView(R.id.item_book_details_case_choose).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.item_book_details_case_choose).setVisibility(View.GONE);
                }
                helper.setText(R.id.item_book_details_case_address, bean.getAddress());
                helper.setText(R.id.item_book_details_case_distance, bean.getRadius() + "km");
            }
        });
        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                current = position;
                fuckYouAdapter.notifyDataSetChanged();
            }
        });
        pullNet();
        getBook();
    }
    /**
     * 更新ui
     */
    private void updateUI() {
        GlideUtils.load(this, bookDetails.getJsonData().getCover(), imgPhoto);
        txName.setText(bookDetails.getJsonData().getName());
        txAuthor.setText("作者: " + bookDetails.getJsonData().getAuthor());
        if (bookDetails != null && bookDetails.getJsonData() != null && bookDetails.getJsonData().getPress() != null) {
            if (bookDetails.getJsonData().getPress().contains("null") ||
                    bookDetails.getJsonData().getPress() == "") {
                txConcern.setText("出版社: 暂无");
            } else {
                txConcern.setText("出版社: " + bookDetails.getJsonData().getPress());
            }
        }
        txConcernTime.setText("出版时间: " + bookDetails.getJsonData().getPublish_time());
    }
    @OnClick({R.id.back_book_pay, R.id.back_book_rule})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_book_pay:
                Intent intent = new Intent(this, OrderBookActivity.class);
                intent.putExtra("orderId", order);
                intent.putExtra("backBook", true);
                startActivity(intent);
                break;
            case R.id.back_book_rule:
                startActivity(new Intent(this, RuleActivity.class));
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
                    BackBookActivity.this.bookDetails = response;
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
    public void pullNet() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<BookCase>("BookCase") {
            @Override
            protected void onStart(Disposable d) {
            }
            @Override
            protected void onError(ApiException e) {
            }
            @Override
            protected void onSuccess(BookCase response) {
                if (response.getJsonData() != null) {
                    list.addAll(response.getJsonData());
                    fuckYouAdapter.notifyDataSetChanged();
                    if (list.size() > 0) {
                        txSubmit.setBackgroundResource(R.drawable.shap_black_7);
                        txSubmit.setClickable(true);
                    }
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("lat", MyApplicationLike.lat);
        map.put("lng", MyApplicationLike.lng);
        map.put("empty_cell", "1");
        map = AppSign.buildMap(map);
        Observable<BookCase> observable = NetUtils.getGsonRetrofit().bookCaseList(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
}
