package dingshi.com.hibook.ui.fragment;


import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.ILoginView;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.WexinInfo;
import dingshi.com.hibook.present.LoginPresent;
import dingshi.com.hibook.share.AuthResult;
import dingshi.com.hibook.share.EasyPayShare;
import dingshi.com.hibook.ui.LoginActivity;
import dingshi.com.hibook.utils.PhoneUtils;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author wangqi
 * @since 2017/11/7 下午3:55
 */


public class LoginFragment extends BaseFragment implements ILoginView {
    private static final int SDK_AUTH_FLAG = 0x2;
    @BindView(R.id.login_mobile)
    EditText mobileEdit;
    @BindView(R.id.login_password)
    EditText pswEdit;
    @BindView(R.id.login_password_show)
    ImageView imgPswShow;
    @BindView(R.id.login_captch_show)
    TextView txCaptch;
    @BindView(R.id.login_captcha)
    TextView txCapchaTip;


    LoginActivity activity;
    LoginPresent loginPresent = new LoginPresent(this, this);

    boolean pswShow = false;


    /**
     * 判断当前登录方式
     */
    boolean isCaptchLogin;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void initView() {
        switchLogin();
        activity = (LoginActivity) mActivity;
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.login_login, R.id.login_register, R.id.login_zhifubao,
            R.id.login_weixin, R.id.login_password_show, R.id.login_captcha,
            R.id.login_captch_show, R.id.login_forget})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login:
                loginPresent.login(mobileEdit.getText().toString().trim(), pswEdit.getText().toString().trim(), isCaptchLogin);
                break;
//            case R.id.login_forget:
//                activity.isforgetPsw = true;
//                activity.switchRegister();
//                break;
//            case R.id.login_register:
//                activity.isforgetPsw = false;
//                activity.switchRegister();
//                break;
            case R.id.login_captcha:
                switchLogin();
                break;
            case R.id.login_zhifubao:
                EasyPayShare.getInstance().doLoginAli(getActivity(), new EasyPayShare.OnBackListener() {
                    @Override
                    public void back(AuthResult authResult) {
                        loginPresent.loginAli(authResult);

                    }
                });

                break;
            case R.id.login_weixin:
                activity.loginWx();
                break;
            case R.id.login_password_show:
                pswShow = !pswShow;
                if (pswShow) {
                    imgPswShow.setImageResource(R.drawable.login_query);
                    pswEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    imgPswShow.setImageResource(R.drawable.login_close);
                    pswEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.login_captch_show:
                loginPresent.captcha(mobileEdit.getText().toString().trim(), "4");
                sendCaptch();
                break;
            default:
        }
    }

    @Override
    public void success() {
        activity.login();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginWx(Map<String, String> map) {
        WexinInfo info = new WexinInfo();
        info.setNickname(map.get("name"));
        info.setHeadimgurl(map.get("profile_image_url"));
        info.setOpenid(map.get("openid"));
        info.setSex(map.get("gender").equals("男") ? 1 : 2);
        info.setCountry(map.get("country"));
        info.setProvince(map.get("province"));
        info.setCity(map.get("city"));
        loginPresent.loginWexin(info);
    }


    @Override
    public void error(String error) {
        showToast(error);
        if (activity.progressDialog != null) {
            activity.dismissProgressDialog();
        }
    }

    @Override
    public void start() {
        activity.showProgressDialog("登录中", true);
    }


    public void switchLogin() {
        isCaptchLogin = !isCaptchLogin;
        if (isCaptchLogin) {
            txCapchaTip.setText("切换密码登录");
            imgPswShow.setVisibility(View.GONE);
            txCaptch.setVisibility(View.VISIBLE);
            pswEdit.setHint("请填写验证码");
            pswEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
            pswEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            txCapchaTip.setText("切换验证码登录");
            pswEdit.setHint("请输入您的密码");
            pswEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgPswShow.setVisibility(View.VISIBLE);
            txCaptch.setVisibility(View.GONE);
        }
    }


    long TIME = 60;
    Subscription s;

    public void sendCaptch() {
        if (!PhoneUtils.isPhoneNumber(mobileEdit.getText().toString().trim())) {
            return;
        }
        Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(TIME - 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return TIME - aLong;
                    }
                })
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        return aLong + "s";
                    }
                }).doOnSubscribe(new Consumer<Subscription>() {
            @Override
            public void accept(Subscription subscription) throws Exception {
                txCaptch.setClickable(false);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        LoginFragment.this.s = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String string) {
                        txCaptch.setText(string + "后重发");
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        txCaptch.setClickable(true);
                        txCaptch.setText("发送验证码");
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (s != null) s.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (s != null) {
            s.cancel();
        }
        EventBus.getDefault().unregister(this);

    }
}
