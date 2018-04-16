package dingshi.com.hibook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import butterknife.BindView;
import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.MyApplicationLike;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.fragment.BackBookFragment;
import dingshi.com.hibook.utils.AppManager;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.view.LoadingLayout;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class LoadingActivity extends BaseActivity {

    @BindView(R.id.loading_photo)
    ImageView logo;


    @Override
    public int getLayoutId() {
        return R.layout.activity_loading;
    }

    /**
     * type : 1、取书打开书柜   跳订单页面
     * 2、共享图书到书柜
     * 3、还书打开书柜   跳评价页面
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "正在取书");

        GlideUtils.load(this, R.drawable.loading, logo);

        switch (getIntent().getIntExtra("type", 0)) {
            case 1:
                borrowBook();
                break;
            case 2:
                shareBook();
                break;
            case 3:
                openCase();
                break;
            default:
        }
    }

    HashMap<String, String> map;

    /**
     * 取书打开书柜   OrderBookActivity
     */
    public void borrowBook() {
        map = (HashMap<String, String>) getIntent().getSerializableExtra("map");
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("openBookCase") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
                showToast(e.getMsg());
                finish();
            }

            @Override
            protected void onSuccess(Result result) {
                showToast(result.getError_msg());
                logo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppManager.getInstance().finishOthersActivity(MainActivity.class);
                        Intent intent = new Intent(LoadingActivity.this, OrderActivity.class);
                        intent.putExtra("borrow", true);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        };
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().openBookCase(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 共享图书到书柜 OrderShareActivity
     */
    public void shareBook() {
        map = (HashMap<String, String>) getIntent().getSerializableExtra("map");
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("bookShare") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                showToast(e.getMsg());
                finish();
            }

            @Override
            protected void onSuccess(Result response) {
                showToast("共享成功");
                AppManager.getInstance().finishOthersActivity(MainActivity.class);
                startActivity(new Intent(LoadingActivity.this, BookHouseActivity.class));
                finish();
            }
        };
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().bookShare(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 还书打开书柜
     */
    private void openCase() {
        map = (HashMap<String, String>) getIntent().getSerializableExtra("map");
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("returnedBook") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                showToast(e.getMsg());
                finish();
            }

            @Override
            protected void onSuccess(Result response) {
                logo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppManager.getInstance().finishOthersActivity(MainActivity.class);
                        Intent intent = new Intent(LoadingActivity.this, EvalFriendActivity.class);
                        intent.putExtra("isbn", map.get("isbn"));
                        intent.putExtra("CommentId", "0");
                        intent.putExtra("openCase", true);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        };
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().returnedBook(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

}
