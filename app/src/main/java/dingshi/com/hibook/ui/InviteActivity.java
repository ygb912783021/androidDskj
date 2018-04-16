package dingshi.com.hibook.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import dingshi.com.hibook.Constant;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.UserCenter;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.JoinBitmapUtils;
import dingshi.com.hibook.view.FuckDialog;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wangqi
 * @since 2018/1/5 下午5:39
 */

public class InviteActivity extends BaseActivity {
    @BindView(R.id.invite_number)
    EditText editNumber;
    @BindView(R.id.invite_invite)
    TextView txInvite;
    @BindView(R.id.invite_alter)
    TextView txAlert;

    UserCenter userCenter;

    String currentInvite;


    @Override
    public int getLayoutId() {
        return R.layout.activity_invite;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "邀请好友", "", R.drawable.invite_share);
        userCenter = (UserCenter) getIntent().getSerializableExtra("bean");

        if (userCenter != null && userCenter.getJsonData().getRevised() > 0) {
            currentInvite = userCenter.getJsonData().getInvite_code();
            txInvite.setText("您的专属邀请码为: " + currentInvite);
            txAlert.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.invite_alter, R.id.invite_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invite_alter:
                showDialog();
                break;
            case R.id.invite_submit:
                submitInvite();
                break;
            default:
        }
    }


    @Override
    public void onRightClick() {
        super.onRightClick();
        if (TextUtils.isEmpty(currentInvite)) {
            showToast("请设置邀请码");
            return;
        }
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


    public void shareBitmap(final Bitmap avatarBitmap) {
        Flowable.create(new FlowableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(FlowableEmitter<Bitmap> e) throws Exception {


                Bitmap logoBitmap = QRCodeEncoder.syncEncodeQRCode(Constant.getInviteUrl(user.getJsonData().getUser_id(), currentInvite), BGAQRCodeUtil.dp2px(InviteActivity.this, 150), Color.BLACK);
                e.onNext(logoBitmap);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {
                        JoinBitmapUtils bitmapUtils = new JoinBitmapUtils();
                        Bitmap bitmap1 = bitmapUtils.getInviteBitmap(InviteActivity.this, bitmap, avatarBitmap, user.getJsonData().getNick_name(), currentInvite);
                        showShareBitmapDialog(bitmap1);
                    }
                });
    }


    /**
     * 设置邀请码
     */
    FuckDialog dialog;
    EditText content;
    String strDialogInvite;

    public void showDialog() {
        View view = LayoutInflater.from(this).inflate(
                R.layout.view_dialog_invite, null, false);
        dialog = new FuckDialog(this)
                .addView(view)
                .builder();
        dialog.show();

        content = view.findViewById(R.id.invite_content);

        view.findViewById(R.id.invite_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strDialogInvite = content.getText().toString();
                if (TextUtils.isEmpty(strDialogInvite)) {
                    showToast("邀请码不能为空");
                    return;
                }
                dialog.dissmis();
                txInvite.setText("您的专属邀请码为: " + strDialogInvite);
                setInviteCode();
            }
        });

        view.findViewById(R.id.invite_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dissmis();
            }
        });
    }

    /**
     * 设置邀请码
     */
    public void setInviteCode() {
        HttpRxObserver<Result> httpRxObserver = new HttpRxObserver<Result>("alterInvite") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                showToast("");
            }

            @Override
            protected void onSuccess(Result response) {
                currentInvite = strDialogInvite;
                txAlert.setVisibility(View.GONE);
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("invite_code", strDialogInvite);
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().alterInvite(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);

    }

    /**
     * 提交邀请码
     */
    String strInviteCode;

    public void submitInvite() {
        strInviteCode = editNumber.getText().toString();
        if (TextUtils.isEmpty(strInviteCode)) {
            showToast("邀请码不能为空");
            return;
        }

        HttpRxObserver<Result> httpRxObserver = new HttpRxObserver<Result>("userInvite") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                if (e.getMsg().contains("请勿重复发送邀请码")){
                    showToast("邀请码重复");
                }else {
                    showToast("邀请码激活失败");
                }

            }

            @Override
            protected void onSuccess(Result response) {
                showToast("邀请成功");
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("invite_code", strInviteCode);
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().userInvite(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);

    }


}
