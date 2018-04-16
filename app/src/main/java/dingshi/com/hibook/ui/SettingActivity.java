package dingshi.com.hibook.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.ISettingView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.hx.DemoHelper;
import dingshi.com.hibook.present.SettingPresent;
import dingshi.com.hibook.utils.AppManager;
import dingshi.com.hibook.utils.SpUtils;

/**
 * @author wangqi
 * @since 2017/11/9 下午4:34
 */

public class SettingActivity extends BaseActivity implements ISettingView {


    @BindView(R.id.setting_version_txt)
    TextView txVersion;


    SettingPresent settingPresent = new SettingPresent(this, this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "设置", "");


        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            txVersion.setText("v" + version);
        } catch (Exception e) {
        }
    }

    @OnClick({R.id.setting_exit, R.id.setting_version, R.id.setting_about, R.id.setting_advice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_exit:
                settingPresent.signOut();
                break;
            case R.id.setting_version:

                break;
            case R.id.setting_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.setting_advice:
                break;

            default:
        }
    }

    /**
     * 退出登录时，关闭所有的页面，打开登录页面  并且退出
     */
    @Override
    public void signOut() {
        //清空本地缓存
        SpUtils.putUser(new User());
        SpUtils.putAllSearch(new ArrayList<String>());
        //退出环信
        DemoHelper.getInstance().logout(false, null);
        AppManager.getInstance().finishAllActivity();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
