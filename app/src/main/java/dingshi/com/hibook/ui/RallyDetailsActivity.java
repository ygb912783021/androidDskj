package dingshi.com.hibook.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
import dingshi.com.hibook.eventbus.EventBusHelper;
import dingshi.com.hibook.eventbus.LibDeleteEvent;
import dingshi.com.hibook.eventbus.LibRefreshEvent;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.card.CreateCardActivity;
import dingshi.com.hibook.ui.fragment.BookListFragment;
import dingshi.com.hibook.ui.fragment.MsgEvalFragment;
import dingshi.com.hibook.ui.fragment.RallyIntroFragment;
import dingshi.com.hibook.ui.library.LibManagerActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.view.SwitchViewPager;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2018/2/1 下午2:31
 */
public class RallyDetailsActivity extends BaseActivity {
    @BindView(R.id.rally_details_tab)
    SwitchViewPager switchViewPager;
    @BindView(R.id.rally_details_vp)
    ViewPager viewPager;
    @BindView(R.id.rally_details_submit)
    TextView txSubmit;

    List<Fragment> list = new ArrayList<>();

    String catalogId;

    String[] title = new String[]{"介绍", "图书列表", "咨询"};


    LibList.JsonDataBean libBean;

    /**
     * 是否是我自己创建的书友会
     */
    boolean isMe;

    @Override
    public int getLayoutId() {
        return R.layout.activity_rally_details;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBusHelper.register(this);

        isMe = getIntent().getBooleanExtra("its_me", false);
        if (isMe) {
            txSubmit.setVisibility(View.GONE);
            requestActionBarStyle(true, "书友会详情", "管理后台");
        } else {
            txSubmit.setVisibility(View.VISIBLE);
            requestActionBarStyle(true, "书友会详情", "");
        }


        catalogId = getIntent().getStringExtra("catalog_id");
        updataUI();
        showLib();

    }


    @OnClick({R.id.rally_details_submit})
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
                    //设置加入状态
                    joinState();
                    cardInfo.setIntro(libBean);
                }
            }
        };

        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalogId);
        map = AppSign.buildMap(map);
        Observable<LibCreate> user = NetUtils.getGsonRetrofit().libIntro(map);
        HttpRxObservable.getObservable(user, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
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

                    if (libBean.getHave_president() == 1) {
                        txSubmit.setText("已加入");
                    } else {
                        txSubmit.setText("申请会长中请等待");
                    }
                    txSubmit.setClickable(false);
                    txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
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

    RallyIntroFragment cardInfo;

    public void updataUI() {
        switchViewPager.bindViewPager(viewPager, title);

        cardInfo = new RallyIntroFragment();
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
        Bundle b3 = new Bundle();
        b3.putString("catalog_id", catalogId);
        bookHouse2.setArguments(b3);
        list.add(bookHouse2);

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), list, title));

    }


    /**
     * 刷新图书馆
     */
    @Subscribe
    public void libRefresh(LibRefreshEvent libEvent) {
        showLib();
    }


    /**
     * 删除图书馆
     */
    @Subscribe
    public void libDelete(LibDeleteEvent libDeleteEvent) {
        finish();
    }


    @Override
    public void onRightClick() {
        if (libBean == null) {
            return;
        }

        Intent intent = new Intent(this, LibManagerActivity.class);
        libBean.setCatalog_id(catalogId);
        intent.putExtra("bean", libBean);
        intent.putExtra("isRally", true);
        startActivity(intent);
    }


    /**
     * 用来获取fragment向上传递当前的状态
     * isJoin 0：已申请 1：已加入 2：已拒绝 3：已退出 4：已被踢 5：从未加入
     * isAllow 图书馆允许加入 0：不允许加入，1：允许加入
     */
    public void joinState() {

        //是否有会长  有会长
        if (libBean.getHave_president() == 1) {
            //已加入
            if (libBean.getIs_join() == 1) {
                txSubmit.setText("已加入");
                txSubmit.setClickable(false);
                txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
                //已拒绝
            } else if (libBean.getIs_join() == 2) {
                txSubmit.setText("您已经被拒绝");
                txSubmit.setClickable(false);
                txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
            } else if (libBean.getIs_join() == 3) {
                txSubmit.setText("加入书友会");
                txSubmit.setClickable(true);
                txSubmit.setBackgroundResource(R.drawable.shap_black);
            } else if (libBean.getIs_join() == 4) {
                txSubmit.setText("您已经被剔出");
                txSubmit.setClickable(false);
                txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
            } else if (libBean.getIs_join() == 5) {
                //不允许加入
                if (libBean.getIs_allow() == 0) {
                    txSubmit.setText("暂时不能加入");
                    txSubmit.setClickable(false);
                    txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
                } else {
                    txSubmit.setText("加入书友会");
                    txSubmit.setClickable(true);
                    txSubmit.setBackgroundResource(R.drawable.shap_black);
                }
            }

        } else {
            //是否已经申请了会长
            if (libBean.getApply_president() == 1) {
                //已经申请会长
                if (libBean.getIs_apply_president() == 1) {
                    txSubmit.setText("申请会长中请等待");
                    txSubmit.setClickable(false);
                    txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
                } else {
                    txSubmit.setText("申请成为会长");
                    txSubmit.setClickable(true);
                    txSubmit.setBackgroundResource(R.drawable.shap_black);
                }

            } else {//不允许
                txSubmit.setText("暂不支持会长申请");
                txSubmit.setClickable(false);
                txSubmit.setBackgroundResource(R.drawable.shap_gray_7);
            }
        }

    }

    AlertDialog dialog;

    public void jumpCard() {
        dialog = new AlertDialog.Builder(this).setMessage("当前没有创建名片,是否创建").setNegativeButton("创建", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(RallyDetailsActivity.this, CreateCardActivity.class));
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        dialog.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unRegister(this);
    }
}
