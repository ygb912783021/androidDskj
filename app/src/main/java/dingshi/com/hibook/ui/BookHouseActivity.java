package dingshi.com.hibook.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.Constant;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.MyViewPagerAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.bean.UserCenter;
import dingshi.com.hibook.eventbus.EventBusHelper;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.fragment.BookHouseFragment;
import dingshi.com.hibook.ui.fragment.FriendRecFragment;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.view.SwitchViewPager;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/11/7 上午11:27
 *
 */


public class BookHouseActivity extends BaseActivity {

    @BindView(R.id.book_house_viewpager)
    ViewPager viewPager;
    @BindView(R.id.book_house_switch)
    SwitchViewPager switchViewPager;
    @BindView(R.id.book_house_photo)
    ImageView imgPhoto;
    @BindView(R.id.book_house_nick)
    TextView txNick;
    @BindView(R.id.book_house_book_num)
    TextView txBookNum;
//    @BindView(R.id.tv_book_house_edit)
//    TextView tv_book_house_edit;
//    boolean isEditDelete=false;


    List<Fragment> list = new ArrayList<>();
    String[] title = new String[]{"书库", "动态"};
    UserCenter userCenter;
    BookHouseFragment houseFragment;
    String user_id="";

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_house;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Log.i("TEST", "initView: .......................................");
        Constant.isEditDelete=false;
        EventBusHelper.register(this);
        requestActionBarStyle(true, "我的书房", "共享规则");
        userCenter = (UserCenter) getIntent().getSerializableExtra("bean");

        switchViewPager.bindViewPager(viewPager, title);

        houseFragment = new BookHouseFragment();
        Bundle b = new Bundle();
        b.putString("uid", user.getJsonData().getUser_id());
        houseFragment.setArguments(b);
        list.add(houseFragment);

        user_id= user.getJsonData().getUser_id();

        Fragment recFragment = new FriendRecFragment();
        list.add(recFragment);

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), list, title));

        GlideUtils.loadCircleImage(BookHouseActivity.this, user.getJsonData().getAvatar(), imgPhoto);
        txNick.setText(user.getJsonData().getNick_name());
        Log.i("TEST", "initView: userCenter = "+ userCenter);
        if (userCenter != null) {
            Log.i("sharebook","已共享="+userCenter.getJsonData().getShare_book_num()+"未共享="+userCenter.getJsonData().getBook_num()+"-"+userCenter.getJsonData().getShare_book_num());
//            txBookNum.setText("已共享:" + userCenter.getJsonData().getShare_book_num() + "\t\t未共享:" + (userCenter.getJsonData().getBook_num() - userCenter.getJsonData().getShare_book_num()));
        }
    }


    @OnClick({R.id.tv_book_house_edit})
    public  void onClickEdit(){
        if (Constant.isEditDelete){
            Constant.isEditDelete=false;
            if (houseFragment!=null){
                houseFragment.updateData(user_id);
            }
        }else {
            Constant.isEditDelete=true;
            if (houseFragment!=null){
                houseFragment.updateData(user_id);
            }
        }
    }
    @OnClick({R.id.book_house_zxing})
    public void onClick(View v) {
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.CAMERA).callback(new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                if (requestCode == 200) {
                    startActivity(new Intent(BookHouseActivity.this, ZxingActivity.class));
                }
            }

            @Override
            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

            }
        }).start();
    }

    @Override
    public void onRightClick() {
        super.onRightClick();
        startActivity(new Intent(this, RuleShareActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
     public void updateBookNum(UserCenter userCenter) {

        Log.i("updateBookNum","dasdasdad");
        this.userCenter=userCenter;
        if (userCenter != null) {
            txBookNum.setText("已共享:" + userCenter.getJsonData().getShare_book_num() + "\t\t未共享:" + (userCenter.getJsonData().getBook_num() - userCenter.getJsonData().getShare_book_num()));
        }
    }

    @Subscribe
    public void updateBookNum(List<BookDetails.JsonDataBean> list){
//        Log.i("list",list.toString());
//        for (int i = 0; i < list.size(); i++) {
//           if (list.get(i).getAvailable()==1){//已共享
//               if (userCenter!=null){
////                   userCenter.getJsonData().setBook_num(userCenter.getJsonData().getBook_num());
//                   userCenter.getJsonData().setShare_book_num(userCenter.getJsonData().getShare_book_num()+1);
//               }
//
//           }else {//未共享
//               userCenter.getJsonData().setBook_num(userCenter.getJsonData().getBook_num()+1);
//           }
//
//        }
//        if (userCenter != null) {
//            userCenter.getJsonData().setBook_num(list.size());
//            userCenter.getJsonData().setBook_num(list.size());
//            txBookNum.setText("已共享:" + userCenter.getJsonData().getShare_book_num() + "\t\t未共享:" + (userCenter.getJsonData().getBook_num() - userCenter.getJsonData().getShare_book_num()));
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        userCenter = (UserCenter) getIntent().getSerializableExtra("bean");
        Log.i("TEST", "onResume: .......................................userCenter = "+userCenter);
        if (userCenter != null) {
            Log.i("sharebook","已共享="+userCenter.getJsonData().getShare_book_num()+"未共享="+userCenter.getJsonData().getBook_num()+"-"+userCenter.getJsonData().getShare_book_num());
//            txBookNum.setText("已共享:" + userCenter.getJsonData().getShare_book_num() + "\t\t未共享:" + (userCenter.getJsonData().getBook_num() - userCenter.getJsonData().getShare_book_num()));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unRegister(this);
    }

    /**
     * 获取用户的id
     */
    public void getUserInfo() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<User>("userShow") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(User bookList) {
                if (bookList.getJsonData() != null) {
                    GlideUtils.loadCircleImage(BookHouseActivity.this, bookList.getJsonData().getAvatar(), imgPhoto);
                    txNick.setText(bookList.getJsonData().getNick_name());
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("uid", String.valueOf(user.getJsonData().getUser_id()));
        map = AppSign.buildMap(map);
        Observable<User> user = NetUtils.getGsonRetrofit().userShow(map);
        HttpRxObservable.getObservable(user, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
}
