package dingshi.com.hibook.ui;

import android.content.Intent;
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
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.MyApplicationLike;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookCase;
import dingshi.com.hibook.bean.BookDetails;
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

public class ShareBookActivity extends BaseActivity {

    @BindView(R.id.share_book_img)
    ImageView imgPhoto;
    @BindView(R.id.share_book_name)
    TextView txBookName;
    @BindView(R.id.share_book_author)
    TextView txBookAuthor;
    @BindView(R.id.share_book_concern)
    TextView txConcern;
    @BindView(R.id.share_book_concern_time)
    TextView txConcernTime;
    @BindView(R.id.share_book_case_label)
    TextView txCase;
    @BindView(R.id.share_book_case)
    RecyclerView recyclerView;

    @BindView(R.id.share_book_submit)
    TextView txSubmit;

    int current = 0;

    BookDetails.JsonDataBean bookDetails;


    FuckYouAdapter fuckYouAdapter;

    List<BookCase.JsonDataBean> list = new ArrayList<>();

    /**
     * 分享类型  0为共享到书柜   1为放在自己手里
     */
    int shareType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_share_book;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        bookDetails = (BookDetails.JsonDataBean) getIntent().getSerializableExtra("bookDetails");
        requestActionBarStyle(true, "选择书柜");
        initBookDetails();
        shareType = getIntent().getIntExtra("share", 0);
        if (shareType > 0) {
            txCase.setText("留在自己手中需要小主您自己跟借书人沟通哦！");
        } else {
            txCase.setText("选择书柜");
            getBookCase();
        }
    }

    public void initBookDetails() {

        GlideUtils.load(this, bookDetails.getCover(), imgPhoto);
        txBookName.setText(bookDetails.getName());
        txBookAuthor.setText("作者: " + bookDetails.getAuthor());
        if (bookDetails != null && bookDetails.getPress() != null) {
            if (bookDetails.getPress().contains("null") || bookDetails.getPress() == "") {
                txConcern.setText("出版社: 暂无");
            } else {
                txConcern.setText("出版社: " + bookDetails.getPress());
            }
        }
        txConcernTime.setText("出版时间: " + bookDetails.getPublish_time());


    }

    /**
     * 书柜
     */
    public void getBookCase() {
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
    }

    @OnClick({R.id.share_book_submit})
    public void onClick(View v) {
        if (shareType == 0) {

            if (list.size() > 0) {
                Intent intent = new Intent(this, OrderShareActivity.class);
                intent.putExtra("bookCase", list.get(current));
                intent.putExtra("bookDetails", bookDetails);
                startActivity(intent);
            } else {
                showToast("当前无书柜");
            }

        } else {
            shareBook(user.getJsonData().getUser_id(), bookDetails.getIsbn());
        }
    }


    /**
     * 获取书柜列表
     */
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
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("lat", MyApplicationLike.lat);
        map.put("lng", MyApplicationLike.lng);
        map = AppSign.buildMap(map);
        Observable<BookCase> observable = NetUtils.getGsonRetrofit().bookCaseList(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    /**
     * 共享图书
     *
     * @param userId   用户id
     * @param bookIsbn Isbn
     */
    public void shareBook(String userId, String bookIsbn) {

        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("bookShare") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {

            }

            @Override
            protected void onSuccess(Result response) {
                showToast("共享成功");
                AppManager.getInstance().finishOthersActivity(MainActivity.class);
                startActivity(new Intent(ShareBookActivity.this, BookHouseActivity.class));

            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", bookIsbn);
        map.put("userId", userId);
        map.put("state", "1");
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().bookShare(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);

    }


}
