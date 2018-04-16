package dingshi.com.hibook.ui.fragment;

import android.content.Intent;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.ILoginView;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.present.LoginPresent;
import dingshi.com.hibook.ui.LoginActivity;
import dingshi.com.hibook.ui.WebActivity;
import dingshi.com.hibook.utils.PhoneUtils;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author wangqi
 * @since 2017/11/7 下午3:54
 */


public class RegisterFragment extends BaseFragment implements ILoginView {

    /**
     * 手机号
     */
    @BindView(R.id.register_mobile)
    EditText mobileEdit;
    /**
     * 验证码
     */
    @BindView(R.id.register_captcha)
    EditText captchaEdit;
    /**
     * 密码
     */
    @BindView(R.id.register_password)
    EditText password;
    /**
     * 显示密码
     */
    @BindView(R.id.register_password_show)
    ImageView imgPswShow;
    /**
     * 发送验证码
     */
    @BindView(R.id.register_captcha_submit)
    TextView txCaptcha;
    /**
     * 登录
     */
    @BindView(R.id.register_register_submit)
    TextView txSubmit;

    @BindView(R.id.register_server_txt)
    TextView txServer;
    boolean pswShow = false;


    LoginActivity activity;
    LoginPresent registerPresent = new LoginPresent(this, this);

    @Override
    public int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void initView() {
        activity = (LoginActivity) mActivity;

        if (activity.isforgetPsw) {
            txSubmit.setText("修改登录");
        } else {
            txSubmit.setText("注册登录");
        }

        SpannableString str = new SpannableString("已阅读并同意《用户服务协议》");
        ForegroundColorSpan fore = new ForegroundColorSpan(0xffe2bc97);
        str.setSpan(fore, 6, str.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        txServer.setText(str);
    }

    @OnClick({R.id.register_server_txt, R.id.register_register_submit, R.id.register_captcha_submit, R.id.register_back, R.id.register_password_show})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_register_submit:
                if (activity.isforgetPsw) {
                    registerPresent.forget(mobileEdit.getText().toString(), captchaEdit.getText().toString(), password.getText().toString());
                } else {
                    registerPresent.register(mobileEdit.getText().toString(), captchaEdit.getText().toString(), password.getText().toString());
                }
                break;
            case R.id.register_captcha_submit:
                captchaEdit.setFocusable(true);
                sendCaptch();
                if (activity.isforgetPsw) {
                    registerPresent.captcha(mobileEdit.getText().toString(), "3");
                } else {
                    registerPresent.captcha(mobileEdit.getText().toString(), "1");
                }
                break;
            case R.id.register_back:
                activity.switchLogin();
                break;
            case R.id.register_password_show:
                pswShow = !pswShow;
                if (pswShow) {
                    imgPswShow.setImageResource(R.drawable.login_query);
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    imgPswShow.setImageResource(R.drawable.login_close);
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.register_server_txt:
                Intent intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("url", "http://m.linkbooker.com/agreement.html");
                startActivity(intent);
                break;

            default:
        }
    }

    @Override
    public void success() {
        activity.login();
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
                txCaptcha.setClickable(false);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        RegisterFragment.this.s = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String string) {
                        if (txCaptcha != null) {
                            txCaptcha.setText(string + "后重发");
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        if (txCaptcha != null) {
                            txCaptcha.setClickable(true);
                            txCaptcha.setText("发送验证码");
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("tag", "onDestroy");

        if (s != null) {
            s.cancel();
        }
    }
}