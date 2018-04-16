package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Order;
import dingshi.com.hibook.bean.OrderDetails;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.hx.ChatActivity;
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
 * @since 2018/1/2 下午6:03
 */

public class OrderPersonActivity extends BaseActivity {

    /**
     * 订单号id
     */
    String orderId;
    /**
     * 图书id
     */
    String isbn;
    /**
     * 用户id
     */
    String uid;
    /**
     * 图书名
     */
    String book;

    @BindView(R.id.order_person_order)
    TextView txOrder;
    @BindView(R.id.order_person_id)
    TextView txCaseId;
    @BindView(R.id.order_person_img)
    ImageView imgBook;
    @BindView(R.id.order_person_book)
    TextView txBookName;
    @BindView(R.id.order_person_author)
    TextView txBookAuthor;
    @BindView(R.id.order_person_concern)
    TextView txConcern;
    @BindView(R.id.order_person_concern_time)
    TextView txConcernTime;
    @BindView(R.id.order_person_letter2)
    TextView txLetter;


    @BindView(R.id.order_person_avatar)
    ImageView imgUserAvatar;
    @BindView(R.id.order_person_nick)
    TextView txUserNick;
    @BindView(R.id.order_person_content)
    TextView txUserContent;


    @Override
    public int getLayoutId() {
        return R.layout.activity_order_person;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "订单详情");
        orderId = getIntent().getStringExtra("orderId");
        isbn = getIntent().getStringExtra("isbn");
        uid = getIntent().getStringExtra("uid");
        book = getIntent().getStringExtra("book");
        getOrderInfo();
        getUser();
        sendMsg();
    }

    /**
     * 更新图书
     *
     * @param response
     */
    private void updateUI(Order.JsonDataBean response) {
        GlideUtils.load(OrderPersonActivity.this, response.getBook().getCover(), imgBook);
        txBookName.setText(response.getBook().getName());
        txBookAuthor.setText("作者: " + response.getBook().getAuthor());
        if (response != null && response.getBook() != null && response.getBook().getPress() != null) {
            if (response.getBook().getPress().contains("null") || response.getBook().getPress() == "") {
                txConcern.setText("出版社: 暂无");
            } else {
                txConcern.setText("出版社: " + response.getBook().getPress());
            }
        }
        txConcernTime.setText("出版时间: " + response.getBook().getPublish_time());
        txOrder.setText(orderId);
        txCaseId.setText(response.getBookcase_cell_id());
        txLetter.setText(response.getRemark());
    }

    @OnClick({R.id.order_person_submit})
    public void onClick(View view) {
        startActivity(new Intent(this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, uid));
    }

    public void sendMsg() {
        EMMessage message = EMMessage.createTxtSendMessage("你好，我刚刚借了你的《" + book + "》这本书，赶快同意呗?", uid);
        message.setAttribute("borrow", "1");
        message.setAttribute("order", orderId);
        message.setAttribute("book", book);
        message.setAttribute("isbn", isbn);
        message.setAttribute("nick", user.getJsonData().getNick_name());
        message.setAttribute("avatar", user.getJsonData().getAvatar());
        message.setAttribute("userid", user.getJsonData().getUser_id());
        EMClient.getInstance().chatManager().sendMessage(message);
    }


    public void getOrderInfo() {
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


    public void getUser() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<User>("userShow") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(User user) {
                GlideUtils.loadCircleImage(OrderPersonActivity.this, user.getJsonData().getAvatar(), imgUserAvatar);
                txUserNick.setText(user.getJsonData().getNick_name());
                txUserContent.setText("读书" + user.getJsonData().getRead_num() + " | 藏书" + user.getJsonData().getBook_num());
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("uid", uid);
        map = AppSign.buildMap(map);
        Observable<User> user = NetUtils.getGsonRetrofit().userShow(map);
        HttpRxObservable.getObservable(user, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    @Override
    public void onLeftClick() {
        super.onLeftClick();
        finishPage();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishPage();
    }

    public void finishPage() {
        AppManager.getInstance().finishOthersActivity(MainActivity.class);
        startActivity(new Intent(OrderPersonActivity.this, MyBorrowActivity.class));
    }
}
