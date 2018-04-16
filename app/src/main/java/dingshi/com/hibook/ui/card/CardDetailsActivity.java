package dingshi.com.hibook.ui.card;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import dingshi.com.hibook.Constant;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.ICardDetails;
import dingshi.com.hibook.adapter.MyViewPagerAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.card.CardDetails;
import dingshi.com.hibook.bean.card.CardList;
import dingshi.com.hibook.db.City;
import dingshi.com.hibook.db.CityDao;
import dingshi.com.hibook.db.SQLTools;
import dingshi.com.hibook.present.CardDetailsPresent;
import dingshi.com.hibook.ui.fragment.BookHouseFragment;
import dingshi.com.hibook.ui.fragment.CardInfoFragment;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.JoinBitmapUtils;
import dingshi.com.hibook.view.CardMoreView;
import dingshi.com.hibook.view.FuckDialog;
import dingshi.com.hibook.view.SwitchViewPager;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wangqi
 * @since 2017/12/19 下午5:08
 */

public class CardDetailsActivity extends BaseActivity implements ICardDetails {
    /**
     * 头像
     */
    @BindView(R.id.card_details_photo)
    ImageView imgPhoto;
    /**
     * 昵称
     */
    @BindView(R.id.card_details_nick)
    TextView txNick;
    /**
     * 职位
     */
    @BindView(R.id.card_details_job)
    TextView txJob;
    /**
     * 公司
     */
    @BindView(R.id.card_details_company)
    TextView txCompany;
    /**
     * 手机
     */
    @BindView(R.id.card_details_mobile)
    TextView txMobile;
    /**
     * 城市
     */
    @BindView(R.id.card_details_city)
    TextView txCity;

    @BindView(R.id.card_details_tab)
    SwitchViewPager switchViewPager;
    @BindView(R.id.card_details_vp)
    ViewPager viewPager;

    @BindView(R.id.card_details_sweep)
    TextView txSweep;

    List<Fragment> list = new ArrayList<>();

    String[] title = new String[]{"基本信息", "图书信息"};
    CardDetailsPresent present = new CardDetailsPresent(this, this);

    /**
     * 名片编号
     */
    String cardId;

    /**
     * 对方的userId
     */
    String toUserId;


    CardDetails.JsonDataBean jsonData;
    /**
     * 判断当前是否是用户自己
     */
    boolean isUser;

    CardInfoFragment cardInfo;
    BookHouseFragment bookHouse;

    @Override
    public int getLayoutId() {
        return R.layout.activity_card_details;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        requestActionBarStyle(true, "名片", "更多");

        cardId = getIntent().getStringExtra("card_id");
        toUserId = getIntent().getStringExtra("uid");

        //判断当前过来的id是自己的还是其他用户
        if (user.getJsonData().getUser_id().equals(toUserId)) {
            requestActionBarStyle(true, "名片", "更多");
            isUser = true;
            toUserId = user.getJsonData().getUser_id();
            txSweep.setText("完善名片");
        } else {
            requestActionBarStyle(true, "名片", "");
            isUser = false;
            txSweep.setText("交换名片");
        }


        switchViewPager.bindViewPager(viewPager, title);
        cardInfo = new CardInfoFragment();
        bookHouse = new BookHouseFragment();
        list.add(cardInfo);
        list.add(bookHouse);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), list, title));

        refresh(null);

    }


    @OnClick({R.id.card_details_share, R.id.card_details_sweep, R.id.card_details_zxing})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_details_share:
                shareCard();
                break;
            case R.id.card_details_sweep:
                if (isUser) {
                    if (jsonData == null) {
                        return;
                    }
                    Intent intent2 = new Intent(this, CreateCardActivity.class);
                    intent2.putExtra("bean", jsonData);
                    startActivity(intent2);
                } else {
                    //交换名片
                    present.sweepCard(toUserId);
                }
                break;
            case R.id.card_details_zxing:
                present.loadZxing(cardId, toUserId);
                break;
            default:
        }
    }


    @Override
    public void onRightClick() {
        super.onRightClick();
        CardMoreView dialog = new CardMoreView(this);
        dialog.show();
        dialog.setOnBackListener(new CardMoreView.OnBackListener() {
            @Override
            public void onBackId(int id) {
                moreClick(id);
            }
        });
    }

    public void moreClick(int id) {
        switch (id) {
            case 1:
                present.loadZxing(cardId, toUserId);
                break;
            case 2:
                if (jsonData == null) {
                    return;
                }
                Intent intent2 = new Intent(this, CreateCardActivity.class);
                intent2.putExtra("bean", jsonData);
                startActivity(intent2);
                break;
            case 3:
                if (jsonData != null && jsonData.getIs_main() == 1) {
                    showToast("当前已是主名片");
                } else {
                    present.setMainCard(cardId);
                }

                break;
            case 4:
                if (jsonData != null && jsonData.getIdentify() == 1) {
                    showToast("已认证成功");
                } else {
                    Intent intent4 = new Intent(this, CardAuthenActivity.class);
                    intent4.putExtra("card_id", cardId);
                    startActivity(intent4);
                }
                break;
            case 5:
                shareCard();
                break;
            default:
        }
    }


    @Override
    public void onCardDetails(CardDetails cardDetails) {
        jsonData = cardDetails.getJsonData();
        GlideUtils.loadCircleImage(this, jsonData.getAvatar(), imgPhoto);
        txNick.setText(jsonData.getName());
        City city = CityDao.queryCity(SQLTools.opendatabase(this), jsonData.getCity());
        txCity.setText("常驻城市: " + city.getName());
        txMobile.setText("手机号: " + jsonData.getPhone());
        txCompany.setText(jsonData.getCompany());
        txJob.setText(jsonData.getPosition());
        updateUI(cardDetails.getJsonData());

        if (!isUser) {
            switch (jsonData.getState()) {
                case 0:
                    txSweep.setText("交换名片");
                    txSweep.setBackgroundResource(R.drawable.shap_black_7);
                    txSweep.setClickable(true);
                    break;
                case 1:
                    txSweep.setText("审核中");
                    txSweep.setBackgroundResource(R.drawable.shap_black_7);
                    break;
                case 2:
                    txSweep.setText("已交换");
                    txSweep.setBackgroundResource(R.drawable.shap_gray_7);
                    break;
                default:
            }
        } else {
            txSweep.setClickable(true);
        }
    }


    public void updateUI(CardDetails.JsonDataBean cardDetails) {
        cardInfo.updateData(cardDetails);
        bookHouse.updateData(toUserId);
    }


    @Override
    public void onMainCard() {
        showToast("设置成功");
    }

    @Override
    public void onError(String error) {
        showToast(error);
    }


    public void shareCard() {
        if (TextUtils.isEmpty(user.getJsonData().getAvatar())) {
            Glide.with(this).load(R.drawable.ease_default_avatar).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    shareBitmap(resource);
                }
            });
        } else {
            Glide.with(this).load(user.getJsonData().getAvatar()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    shareBitmap(resource);
                }
            });

        }
    }


    public void shareBitmap(final Bitmap resource) {
        Flowable.create(new FlowableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(FlowableEmitter<Bitmap> e) throws Exception {
                Bitmap logoBitmap = QRCodeEncoder.syncEncodeQRCode(Constant.getInviteUrl(user.getJsonData().getUser_id(), ""), BGAQRCodeUtil.dp2px(CardDetailsActivity.this, 150), Color.BLACK);
                e.onNext(logoBitmap);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {

                        JoinBitmapUtils bitmapUtils = new JoinBitmapUtils();
                        String mobile = user.getJsonData().getMobile();

                        if (mobile != null && mobile != "") {
                            mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
                        }
//                      mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
                        String nick = user.getJsonData().getNick_name();
                        if (nick.length() > 6) {
                            nick = nick.substring(0, 7) + "...";
                        }
                        nick = "'" + nick + "'";
                        Bitmap bitmap1 = bitmapUtils.getCardBitmap(CardDetailsActivity.this, bitmap, resource, mobile, nick);
                        showShareBitmapDialog(bitmap1);
                    }
                });
    }


    /**
     * 显示二维码dialog
     */
    FuckDialog fuckDialog;

    @Override
    public void onLoadZxing(Bitmap bitmap) {
        if (jsonData == null) {
            return;
        }

        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_zxing, null, false);

        ImageView zxingImg = view.findViewById(R.id.dialog_zixng_img);
        ImageView imgPhoto = view.findViewById(R.id.dialog_zixng_photo);
        TextView nickText = view.findViewById(R.id.dialog_zixng_nick);
        TextView jobText = view.findViewById(R.id.dialog_zixng_job);
        TextView companyText = view.findViewById(R.id.dialog_zixng_company);

        zxingImg.setImageBitmap(bitmap);
        GlideUtils.loadCircleImage(this, jsonData.getAvatar(), imgPhoto);
        nickText.setText(jsonData.getName());
        companyText.setText(jsonData.getCompany());
        jobText.setText(jsonData.getPosition());


        fuckDialog = new FuckDialog(this).addView(view).builder();
        fuckDialog.show();
    }


    @Override
    public void onSweep(CardDetails cardDetails) {
        EMMessage message = EMMessage.createTxtSendMessage("交换名片啦", toUserId);//526741470357491713
        message.setAttribute("borrow", "6000");
        message.setAttribute("apply_id", cardId);
        message.setAttribute("nick", user.getJsonData().getNick_name());
        message.setAttribute("avatar", user.getJsonData().getAvatar());
        message.setAttribute("userid", cardId);
        EMClient.getInstance().chatManager().sendMessage(message);

        txSweep.setText("审核中");
        txSweep.setBackgroundResource(R.drawable.shap_black_7);
        txSweep.setClickable(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(CardList cardList) {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("card_id", cardId);
        map.put("uid", toUserId);
        present.getCardDetails(map);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
