package dingshi.com.hibook.ui.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.Constant;
import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.BookList;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.bean.UserCenter;
import dingshi.com.hibook.hx.ChatActivity;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.AddressBookActivity;
import dingshi.com.hibook.ui.InviteActivity;
import dingshi.com.hibook.ui.MyBorrowActivity;
import dingshi.com.hibook.ui.BookHouseActivity;
import dingshi.com.hibook.ui.CouponActivity;
import dingshi.com.hibook.ui.MoneyBagActivity;
import dingshi.com.hibook.ui.OrderActivity;
import dingshi.com.hibook.ui.SettingActivity;
import dingshi.com.hibook.ui.UserActivity;
import dingshi.com.hibook.ui.card.MyCardActivity;
import dingshi.com.hibook.ui.library.LibActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.SpUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 */
public class BookMyFragment extends BaseFragment {
    @BindView(R.id.book_my_photo)
    ImageView myPhoto;

    @BindView(R.id.book_my_money_txt)
    TextView txMoney;
    @BindView(R.id.book_my_borrow_txt)
    TextView txBorrow;
    @BindView(R.id.book_my_house_txt)
    TextView txHouseNum;

    @BindView(R.id.book_my_order_txt)
    TextView txOrder;
    @BindView(R.id.book_my_coupon_txt)
    TextView txCoupon;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_book_my;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        GlideUtils.loadCircleImage(mActivity, user.getJsonData().getAvatar(), myPhoto);
        txMoney.setText(user.getJsonData().getBalance());
    }


    @OnClick({R.id.book_my_card, R.id.book_my_lib,
            R.id.book_my_photo, R.id.book_my_money,
            R.id.book_my_order, R.id.book_my_borrow,
            R.id.book_my_book_house, R.id.book_my_coupon,
            R.id.book_my_invite, R.id.book_my_setting,
            R.id.book_my_address_book, R.id.book_my_rally
    })
    public void onClickActivity(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.book_my_photo:
                intent.setClass(mActivity, UserActivity.class);
                break;
            case R.id.book_my_money:
                intent.setClass(mActivity, MoneyBagActivity.class);
                break;
            case R.id.book_my_order:
                intent.setClass(mActivity, OrderActivity.class);
                break;
            case R.id.book_my_borrow:
                intent.setClass(mActivity, MyBorrowActivity.class);
                break;
            case R.id.book_my_book_house:
                intent.setClass(mActivity, BookHouseActivity.class);
                intent.putExtra("bean", userCenter);
                break;
            case R.id.book_my_coupon:
                intent.setClass(mActivity, CouponActivity.class);
                break;
            case R.id.book_my_setting:
                intent.setClass(mActivity, SettingActivity.class);
                break;
            case R.id.book_my_card:
                intent.setClass(mActivity, MyCardActivity.class);
                break;
            case R.id.book_my_lib:
                intent.setClass(mActivity, LibActivity.class);
                intent.putExtra("isRally", false);
                break;
            case R.id.book_my_rally:
                intent.setClass(mActivity, LibActivity.class);
                intent.putExtra("isRally", true);
                break;
            case R.id.book_my_invite:
                intent.setClass(mActivity, InviteActivity.class);
                intent.putExtra("bean", userCenter);
                break;
            case R.id.book_my_address_book:
                intent.setClass(mActivity, AddressBookActivity.class);
                break;
            default:
        }
        startActivity(intent);
    }

    @OnClick(R.id.book_my_msg)
    public void onClick(View view) {
        MainActivity activity = (MainActivity) mActivity;
        activity.setTabSelection(4);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlideUtils.loadCircleImage(mActivity, SpUtils.getUser().getJsonData().getAvatar(), myPhoto);
        getUserCenter();
    }

    /**
     * 获取"我的"信息
     */
    UserCenter userCenter;

    public void getUserCenter() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<UserCenter>("configure") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(UserCenter userCenter) {
                BookMyFragment.this.userCenter = userCenter;
                txBorrow.setText(userCenter.getJsonData().getNo_pickup_num() + "本");
                txOrder.setText(userCenter.getJsonData().getOrder_num());
                txCoupon.setText(userCenter.getJsonData().getCoupon_num());
                txHouseNum.setText(userCenter.getJsonData().getBook_num() + "本");
                //将获取到的押金状态存储到本地
                User user = SpUtils.getUser();
                user.getJsonData().setBalance_refund_status(userCenter.getJsonData().getBalance_refund_status());
                SpUtils.putUser(user);
            }
        };

        HashMap<String, String> map = new HashMap<>(2);

        map = AppSign.buildMap(map);

        Observable<UserCenter> user = NetUtils.getGsonRetrofit().configure(map);

        HttpRxObservable.getObservable(user, this, FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }


    @Subscribe
    public void refresh(User u) {
        txMoney.setText(u.getJsonData().getBalance());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
