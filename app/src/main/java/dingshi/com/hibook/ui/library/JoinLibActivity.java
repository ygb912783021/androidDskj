package dingshi.com.hibook.ui.library;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.MyViewPagerAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.card.CardList;
import dingshi.com.hibook.bean.lib.LibCreate;
import dingshi.com.hibook.bean.lib.LibList;
import dingshi.com.hibook.eventbus.LibRefreshEvent;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.card.CreateCardActivity;
import dingshi.com.hibook.ui.fragment.BookListFragment;
import dingshi.com.hibook.ui.fragment.LibIntroFragment;
import dingshi.com.hibook.ui.fragment.MsgEvalFragment;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.view.SwitchViewPager;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/20 下午5:31
 */

public class JoinLibActivity extends BaseActivity {

    //    @BindView(R.id.join_lib_banner)
//    ImageView banner;
    @BindView(R.id.join_lib_tab)
    SwitchViewPager switchViewPager;
    @BindView(R.id.join_lib_vp)
    ViewPager viewPager;
    @BindView(R.id.join_lib_submit)
    TextView txSubmit;

    List<Fragment> list = new ArrayList<>();

    String catalogId;
    String catalogImgUrl;

    String[] title = new String[]{"介绍", "图书列表", "咨询"};


    LibList.JsonDataBean libBean;


    @Override
    public int getLayoutId() {
        return R.layout.activity_join_lib;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "图书馆详情", "");
        catalogId = getIntent().getStringExtra("catalog_id");
        updataUI();
        showLib();
    }

    @OnClick({R.id.join_lib_submit})
    public void onClick(View view) {
        pullNet();
    }


    public void showLib() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<LibCreate>("libIntro") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(LibCreate libIntro) {
                if (libIntro.getJsonData() != null) {
                    libBean = libIntro.getJsonData();
                    cardInfo.setIntro(libBean);
                    joinState(libBean.getIs_join());
                }
            }
        };

        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalogId);
        map = AppSign.buildMap(map);
        Observable<LibCreate> user = NetUtils.getGsonRetrofit().libIntro(map);
        HttpRxObservable.getObservable(user, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    LibIntroFragment cardInfo;

    private void updataUI() {
        switchViewPager.bindViewPager(viewPager, title);

        cardInfo = new LibIntroFragment();
        Bundle b = new Bundle();
        b.putString("catalog_id", catalogId);
        cardInfo.setArguments(b);
        list.add(cardInfo);

        Fragment bookList = new BookListFragment();
        Bundle b2 = new Bundle();
        b2.putString("catalog_id", catalogId);
        bookList.setArguments(b2);
        list.add(bookList);

        Fragment bookHouse2 = new MsgEvalFragment();
        list.add(bookHouse2);

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), list, title));

    }


    public void pullNet() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("libJoin") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                showToast(e.getMsg());
            }

            @Override
            protected void onSuccess(Result response) {
                if (response.getError_code() == 0) {
                    jumpCard();
                } else {
                    joinState(0);
                    EventBus.getDefault().post(new LibRefreshEvent());
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalogId);
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().libJoin(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 用来获取fragment向上传递当前的状态
     * isJoin 0：已申请 1：已加入 2：已拒绝 3：已退出 4：已被踢 5：从未加入
     * isAllow 图书馆允许加入 0：不允许加入，1：允许加入
     */
    public void joinState(int isJoin) {

        if (libBean.getIs_allow() == 0) {
            txSubmit.setText("当前不允许加入");
            txSubmit.setClickable(false);
            txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
        } else {
            if (isJoin == 0) {
                txSubmit.setText("已加入");
                txSubmit.setClickable(false);
                txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
            } else if (isJoin == 1) {
                txSubmit.setText("已申请");
                txSubmit.setClickable(false);
                txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
            } else if (isJoin == 2) {
                txSubmit.setText("已拒绝");
                txSubmit.setClickable(false);
                txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
            } else if (isJoin == 3) {
                txSubmit.setText("已退出");
                txSubmit.setClickable(false);
                txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
            } else if (isJoin == 4) {
                txSubmit.setText("已被踢");
                txSubmit.setClickable(false);
                txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
            } else if (isJoin == 5) {
                txSubmit.setText("申请加入");
                txSubmit.setClickable(true);
                txSubmit.setBackgroundResource(R.drawable.shap_black);
            }
        }


    }

    AlertDialog dialog;

    public void jumpCard() {
        dialog = new AlertDialog.Builder(this).setMessage("当前没有创建名片,是否创建").setNegativeButton("创建", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(JoinLibActivity.this, CreateCardActivity.class));
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        dialog.show();
    }
}
