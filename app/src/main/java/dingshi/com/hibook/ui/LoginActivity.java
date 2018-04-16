package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.ui.fragment.LoginFragment;
import dingshi.com.hibook.ui.fragment.RegisterFragment;
import dingshi.com.hibook.utils.SpUtils;

/**
 * @author wangqi
 * @since 2017/11/7 下午3:38
 */


public class LoginActivity extends BaseActivity {

    private FragmentManager fragmentManager;

    LoginFragment loginFragment;
    RegisterFragment registerFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        hideTitleBar();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (getIntent().getBooleanExtra("register", false)) {
            registerFragment = new RegisterFragment();
            transaction.replace(R.id.login_layout, registerFragment);
        } else {
            loginFragment = new LoginFragment();
            transaction.replace(R.id.login_layout, loginFragment);
        }
        transaction.commit();
    }

    public void switchLogin() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out);
        loginFragment = new LoginFragment();
        transaction.replace(R.id.login_layout, loginFragment);
        transaction.commit();
    }


    public void switchRegister() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out);
        registerFragment = new RegisterFragment();
        transaction.replace(R.id.login_layout, registerFragment);
        transaction.commit();
    }

    public boolean isforgetPsw;

    public boolean getForgetPsw() {
        return isforgetPsw;
    }


    public void onFinish() {
        finish();
    }


    public void login() {

        EMClient.getInstance().login(SpUtils.getUser().getJsonData().getUser_id(), "hellobook", new EMCallBack() {

            @Override
            public void onSuccess() {
                dismissProgressDialog();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        Toast.makeText(getApplicationContext(), "登录IM失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    /**
     * 拿到微信返回的信息
     *
     * @param map
     */
    @Override
    public void loginSuccess(Map<String, String> map) {
        super.loginSuccess(map);
        EventBus.getDefault().post(map);
    }

}
