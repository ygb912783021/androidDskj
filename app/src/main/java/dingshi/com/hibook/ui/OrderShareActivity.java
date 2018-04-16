package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.MyApplicationLike;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookCase;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.utils.GlideUtils;

public class OrderShareActivity extends BaseActivity {
    @BindView(R.id.order_share_id)
    TextView txCaseId;
    @BindView(R.id.order_share_img)
    ImageView imgPhoto;
    @BindView(R.id.order_share_name)
    TextView txBookName;
    @BindView(R.id.order_share_author)
    TextView txBookAuthor;
    @BindView(R.id.order_share_concern)
    TextView txConcern;
    @BindView(R.id.order_share_concern_time)
    TextView txConcernTime;
    @BindView(R.id.order_share_address)
    TextView txAddress;

    BookDetails.JsonDataBean bookDetails;
    BookCase.JsonDataBean bookCase;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_share;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "共享到书柜");
        bookDetails = (BookDetails.JsonDataBean) getIntent().getSerializableExtra("bookDetails");
        bookCase = (BookCase.JsonDataBean) getIntent().getSerializableExtra("bookCase");


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
        txCaseId.setText(bookCase.getSerial_number());
        txAddress.setText(bookCase.getAddress());
    }


    @OnClick({R.id.order_share_submit})
    public void onClick(View view) {
        Intent intent = new Intent(this, LoadingActivity.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("serial_number", bookCase.getSerial_number());
        map.put("isbn", bookDetails.getIsbn());
        map.put("state", "1");
        map.put("lat", MyApplicationLike.lat);
        map.put("lng", MyApplicationLike.lng);

        intent.putExtra("type", 2);
        intent.putExtra("map", map);
        startActivity(intent);
        finish();
    }
}
