package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.View;
import android.widget.EditText;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.CommGradeAdd;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.CheckEmoJi;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/13 上午9:55
 */


public class EvalBookActivity extends BaseActivity {
    public static final int EVAL_BOOK_REQUEST = 0x1129;
    @BindView(R.id.eval_book_content)
    EditText editContent;
    @BindView(R.id.eval_book_rating)
    AppCompatRatingBar ratingBar;

    String bookIsbn;
    String strContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_eval_book;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "书友评分", "");
        bookIsbn = getIntent().getStringExtra("isbn");
    }


    @OnClick({R.id.eval_book_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.eval_book_submit:
                strContent = editContent.getText().toString();
                if (!CheckEmoJi.containsEmoji(strContent)){
                    submit();
                }
                break;
            default:
        }
    }


    public void submit() {
        if ("".equals(strContent)) {
            showToast("评分内容不能为空");
            return;
        }


        HttpRxObserver httpRxObserver = new HttpRxObserver<CommGradeAdd>("CommGradeAdd") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                showToast("您已评论过");
            }

            @Override
            protected void onSuccess(CommGradeAdd response) {

                Intent intent = getIntent();
                intent.putExtra("CommGradeAdd", response.getJsonData());
                setResult(EVAL_BOOK_REQUEST, intent);
                finish();
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", bookIsbn);
        map.put("score", (ratingBar.getRating()*2) + "");
        map.put("content", strContent);
        map = AppSign.buildMap(map);
        Observable<CommGradeAdd> observable = NetUtils.getGsonRetrofit().addCommonGrade(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
}
