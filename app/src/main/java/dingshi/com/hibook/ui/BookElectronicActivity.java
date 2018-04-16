package dingshi.com.hibook.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.GridView;

import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * @author yangguangbing
 * @since 2018/4/9
 *  电子书领取页面
 */


public class BookElectronicActivity extends BaseActivity {

    @BindView(R.id.book_electroic_gridview)
    GridView electroic_gridview;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_electronic_shelf;
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        Log.i("TEST", "initView: .......................................");
        Constant.isEditDelete=false;
        EventBusHelper.register(this);
        requestActionBarStyle(true, "电子书柜", "");
        initData();
        int[] to={R.id.electronic_gridview_img,R.id.electronic_gridview_text};
        adapter=new SimpleAdapter(this, dataList, R.layout.view_electronic_gridview_item, new String[]{"img","text"}, to);
        electroic_gridview.setAdapter(adapter);
    }

    /**
     * 数据获取(假数据)
     */
    void initData() {
        //图标
        int icno[] = { R.drawable.books, R.drawable.books,
                R.drawable.books, R.drawable.books
                , R.drawable.books, R.drawable.books
                };
        //图标下的文字
        String name[]={"第一个","第二个","第三个","第四个","第五个","第六个"};
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <icno.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", icno[i]);
            map.put("text",name[i]);
            dataList.add(map);
        }
    }

//    @OnClick({R.id.tv_book_house_edit})
//    public  void onClickEdit(){
//        if (Constant.isEditDelete){
//            Constant.isEditDelete=false;
////            if (houseFragment!=null){
////                houseFragment.updateData(user_id);
////            }
//        }else {
//            Constant.isEditDelete=true;
////            if (houseFragment!=null){
////                houseFragment.updateData(user_id);
////            }
//        }
//    }
    @OnClick({R.id.electroic_gridview_text})
    public void onClick(View v) {
        startActivity(new Intent(this,BookElectronicCaseActivity.class));
        finish();
    }
//
//    @Override
//    public void onRightClick() {
//        super.onRightClick();
//        startActivity(new Intent(this, RuleShareActivity.class));
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//     public void updateBookNum(UserCenter userCenter) {
//
//        Log.i("updateBookNum","dasdasdad");
////        this.userCenter=userCenter;
//        if (userCenter != null) {
////            txBookNum.setText("已共享:" + userCenter.getJsonData().getShare_book_num() + "\t\t未共享:" + (userCenter.getJsonData().getBook_num() - userCenter.getJsonData().getShare_book_num()));
//        }
//    }

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
//        userCenter = (UserCenter) getIntent().getSerializableExtra("bean");
//        Log.i("TEST", "onResume: .......................................userCenter = "+userCenter);
//        if (userCenter != null) {
//            Log.i("sharebook","已共享="+userCenter.getJsonData().getShare_book_num()+"未共享="+userCenter.getJsonData().getBook_num()+"-"+userCenter.getJsonData().getShare_book_num());
////            txBookNum.setText("已共享:" + userCenter.getJsonData().getShare_book_num() + "\t\t未共享:" + (userCenter.getJsonData().getBook_num() - userCenter.getJsonData().getShare_book_num()));
//        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unRegister(this);
    }

    /**
     * 获取用户的id
     */
//    public void getUserInfo() {
//        HttpRxObserver httpRxObserver = new HttpRxObserver<User>("userShow") {
//            @Override
//            protected void onStart(Disposable d) {
//            }
//
//            @Override
//            protected void onError(ApiException e) {
//            }
//
//            @Override
//            protected void onSuccess(User bookList) {
//                if (bookList.getJsonData() != null) {
////                    GlideUtils.loadCircleImage(BookElectronicActivity.this, bookList.getJsonData().getAvatar(), imgPhoto);
////                    txNick.setText(bookList.getJsonData().getNick_name());
//                }
//            }
//        };
//        HashMap<String, String> map = new HashMap<>(2);
//        map.put("uid", String.valueOf(user.getJsonData().getUser_id()));
//        map = AppSign.buildMap(map);
//        Observable<User> user = NetUtils.getGsonRetrofit().userShow(map);
//        HttpRxObservable.getObservable(user, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
//    }
}
