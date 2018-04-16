package dingshi.com.hibook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.CommGradeAdd;
import dingshi.com.hibook.bean.CommInfoAdd;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppManager;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/13 上午11:15
 */


public class EvalFriendActivity extends BaseActivity {

    public static final int EVAL_FRIEND_REQUEST = 0x1027;
    @BindView(R.id.eval_friend_content)
    EditText editContent;

    /**
     * 评论id
     */
    String strCommentId;
    /**
     * 评论内容
     */
    String strContent;
    /**
     * 图书的isbn
     */
    String bookIsbn;

    /**
     * 是否是取书过来的评价
     */
    boolean isOpenCase;

    @Override
    public int getLayoutId() {
        return R.layout.activity_eval_friend;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "书友交流", "");
        Intent intent = getIntent();
        strCommentId = intent.getStringExtra("CommentId");
        bookIsbn = intent.getStringExtra("isbn");

        //判断是否是还书完成跳转的评价页面
        if (getIntent().getBooleanExtra("openCase", false)) {
            isOpenCase = true;
        }
    }


    @OnClick({R.id.eval_friend_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.eval_friend_submit:
                submit();
                break;
            default:
        }
    }

    /**
     * 提交
     */
    private void submit() {
        strContent = editContent.getText().toString();
        if ("".equals(strContent)) {
            showToast("评论内容不能为空");
            return;
        }
        HttpRxObserver httpRxObserver = new HttpRxObserver<CommInfoAdd>("CommInfoAdd") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                showToast(e.getMsg());
            }

            @Override
            protected void onSuccess(CommInfoAdd response) {
                Intent intent = getIntent();
                intent.putExtra("CommInfoAdd", response.getJsonData());
                setResult(EVAL_FRIEND_REQUEST, intent);
                finish();

                if (isOpenCase) {
                    AppManager.getInstance().finishOthersActivity(MainActivity.class);
                    Intent intent2 = new Intent(EvalFriendActivity.this, MyBorrowActivity.class);
                    intent2.putExtra("switch", true);
                    startActivity(intent2);
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", bookIsbn);
        map.put("comment_id", strCommentId);
        map.put("content", strContent);
        map = AppSign.buildMap(map);
        Observable<CommInfoAdd> observable = NetUtils.getGsonRetrofit().addCommonInfo(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

}
