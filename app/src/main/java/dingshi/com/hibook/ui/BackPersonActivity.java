package dingshi.com.hibook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.BorrowRefresh;
import dingshi.com.hibook.bean.Order;
import dingshi.com.hibook.bean.Result;
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
 * @since 2017/12/29 上午9:51
 */
public class BackPersonActivity extends BaseActivity {

    @BindView(R.id.back_person_img)
    ImageView imgBook;
    @BindView(R.id.back_person_book)
    TextView txBookName;
    @BindView(R.id.back_person_author)
    TextView txBookAuthor;
    @BindView(R.id.back_person_concern)
    TextView txConcern;
    @BindView(R.id.back_person_concern_time)
    TextView txConcernTime;

    @BindView(R.id.back_person_avatar)
    ImageView imgUserAvatar;
    @BindView(R.id.back_person_nick)
    TextView txUserNick;
    @BindView(R.id.back_person_content)
    TextView txUserContent;

    //确认取书
    @BindView(R.id.back_person_right)
    TextView txRightReceive;
    //联系取书   联系还书
    @BindView(R.id.back_person_submit)
    TextView txConversation;


    String isbn;
    String uid;
    String orderId;
    String bookName = "";


    /**
     * 判断是取书还是还书   取书是false  还书是true
     */
    boolean isReceive;

    @Override
    public int getLayoutId() {
        return R.layout.activity_back_person;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        isReceive = getIntent().getBooleanExtra("receive", false);
        if (isReceive) {
            requestActionBarStyle(true, "联系还书", "确认还书");
            txRightReceive.setVisibility(View.GONE);
            txConversation.setText("私信还书");
        } else {
            requestActionBarStyle(true, "联系取书", "确认取书");
            txConversation.setText("私信取书");
        }

        isbn = getIntent().getStringExtra("isbn");
        uid = getIntent().getStringExtra("uid");
        orderId = getIntent().getStringExtra("orderId");
        getBook();
        getUser();
    }

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
                    GlideUtils.load(BackPersonActivity.this, response.getJsonData().getCover(), imgBook);
                    bookName = response.getJsonData().getName();
                    txBookName.setText(response.getJsonData().getName());
                    if (response!=null&&response.getJsonData()!=null&&response.getJsonData().getPress()!=null){
                        if (response.getJsonData().getAuthor().contains("null")||
                                response.getJsonData().getPress()==""){
                            txConcern.setText("出版社: 暂无");
                        }else {
                            txConcern.setText("出版社: " + response.getJsonData().getPress());
                        }
                    }
                    txConcernTime.setText("出版时间: " + response.getJsonData().getPublish_time());
                    txBookAuthor.setText("作者: " + response.getJsonData().getAuthor());
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", isbn);
        map = AppSign.buildMap(map);
        Observable<BookDetails> observable = NetUtils.getGsonRetrofit().bookShow(map);
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
                GlideUtils.loadCircleImage(BackPersonActivity.this, user.getJsonData().getAvatar(), imgUserAvatar);
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


    @OnClick({R.id.back_person_rule, R.id.back_person_submit, R.id.back_person_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_person_rule:
                startActivity(new Intent(this, RuleActivity.class));
                break;
            //联系取书
            case R.id.back_person_submit:
                conversation();
                break;
            //确认取书
            case R.id.back_person_right:

                break;
            default:
        }
    }


    @Override
    public void onRightClick() {
        super.onRightClick();

        if (isReceive) {
            rightBackBook();
        } else {
            rightReceive();
        }

    }


    public void rightBackBook() {
        EMMessage message = EMMessage.createTxtSendMessage("这本书我已经看完了，很棒的一本书。请确认我已将这本书《" + bookName + "》还给您？", uid);//526692416898469894
        message.setAttribute("borrow", "100");
        message.setAttribute("order", orderId);
        message.setAttribute("book", bookName);
        message.setAttribute("nick", user.getJsonData().getNick_name());
        message.setAttribute("avatar", user.getJsonData().getAvatar());
        message.setAttribute("userid", user.getJsonData().getUser_id());
        EMClient.getInstance().chatManager().sendMessage(message);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(EaseConstant.EXTRA_USER_ID, uid);
        startActivity(intent);
        finish();
        AppManager.getInstance().finishOthersActivity(MainActivity.class);
    }
    /**
     * 确认取书  请求后台 并且发送一条消息给对方
     */
    public void rightReceive() {
        EMMessage message = EMMessage.createTxtSendMessage("这本《" + bookName + "》我已经拿到了正在阅读，谢谢您!", uid);
        message.setAttribute("nick", user.getJsonData().getNick_name());
        message.setAttribute("avatar", user.getJsonData().getAvatar());
        message.setAttribute("userid", user.getJsonData().getUser_id());
        EMClient.getInstance().chatManager().sendMessage(message);
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("userShow") {
            @Override
            protected void onStart(Disposable d) {
            }
            @Override
            protected void onError(ApiException e) {
                showToast(e.getMsg());
            }
            @Override
            protected void onSuccess(Result result) {
//                AppManager.getInstance().finishOthersActivity(MainActivity.class);
//                Intent intent = new Intent(BackPersonActivity.this, MyBorrowActivity.class);
//                startActivity(intent);

                EventBus.getDefault().post(new BorrowRefresh());
                finish();
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("uid", uid);
        map.put("out_trade_no", orderId);
        map = AppSign.buildMap(map);
        Observable<Result> user = NetUtils.getGsonRetrofit().receiveBook(map);
        HttpRxObservable.getObservable(user, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    /**
     * 联系取书和联系还书操作
     */
    public void conversation() {

        if (isReceive) {

            EMMessage message = EMMessage.createTxtSendMessage("这本书我已经看完了，很棒的一本书。你看下什么时间，在哪里把这本《" + bookName + "》还给您？", uid);//526692416898469894
            message.setAttribute("order", orderId);
            message.setAttribute("book", bookName);
            message.setAttribute("nick", user.getJsonData().getNick_name());
            message.setAttribute("avatar", user.getJsonData().getAvatar());
            message.setAttribute("userid", user.getJsonData().getUser_id());
            EMClient.getInstance().chatManager().sendMessage(message);


            AppManager.getInstance().finishOthersActivity(MainActivity.class);

            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(EaseConstant.EXTRA_USER_ID, uid);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(EaseConstant.EXTRA_USER_ID, uid);
            startActivity(intent);
        }
    }
}
