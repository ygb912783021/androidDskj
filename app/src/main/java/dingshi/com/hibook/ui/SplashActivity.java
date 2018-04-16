package dingshi.com.hibook.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.igexin.sdk.PushManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.OnClick;
import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.MyApplication;
import dingshi.com.hibook.MyApplicationLike;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Push;
import dingshi.com.hibook.db.City;
import dingshi.com.hibook.db.CityDao;
import dingshi.com.hibook.db.SQLTools;
import dingshi.com.hibook.push.DemoIntentService;
import dingshi.com.hibook.push.DemoPushService;
import dingshi.com.hibook.push.PushUtils;
import dingshi.com.hibook.ui.card.CardDetailsActivity;
import dingshi.com.hibook.ui.library.JoinLibActivity;
import dingshi.com.hibook.utils.LocationUtils;
import dingshi.com.hibook.utils.SpUtils;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * @author wangqi
 * @since 2017/11/9 下午6:05
 */


public class SplashActivity extends BaseActivity {

    private Class userPushService = DemoPushService.class;
    private static final int REQUEST_PERMISSION = 0x007;

    /**
     * 个推过来的数据
     */
    String strPush;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        hideTitleBar();

        strPush = getIntent().getStringExtra("push");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        registerPush();

        LocationUtils.getInstance().registerLocation(getApplication(), new LocationUtils.LocationListner() {
            @Override
            public void location(double latitude, double longtitude) {
                MyApplicationLike.lat = String.valueOf(latitude);
                MyApplicationLike.lng = String.valueOf(longtitude);
            }
        });


    }

    /**
     * 注册个推push
     */
    private void registerPush() {
        if (Build.VERSION.SDK_INT >= 21) {
            requestPermission();
        } else {
            jumpActivity();
            PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
        }
        // 注册 intentService 后 PushDemoReceiver 无效, sdk 会使用 DemoIntentService 传递数据,
        // AndroidManifest 对应保留一个即可(如果注册 DemoIntentService, 可以去掉 PushDemoReceiver, 如果注册了
        // IntentService, 必须在 AndroidManifest 中声明)
        PushManager.getInstance().registerPushIntentService(getApplicationContext(), DemoIntentService.class);

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(getApplicationContext(), userPushService);
            } else {
                PushManager.getInstance().initialize(getApplicationContext(), userPushService);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        jumpActivity();
    }


    public void jumpActivity() {
        Flowable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                        if (EMClient.getInstance().isLoggedInBefore() && user != null && user.getJsonData() != null) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            PushUtils pushUtils = new PushUtils();
                            pushUtils.judge(SplashActivity.this, strPush);
                            //如果没有启动页，打开
                        } else if (!SpUtils.getBootPage()) {
                            SpUtils.setBootPage();
                            startActivity(new Intent(SplashActivity.this, BootPageActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }
                        finish();
                    }
                });
    }


    @Override
    protected void onPause() {
        LocationUtils.getInstance().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocationUtils.getInstance().onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationUtils.getInstance().unRegisterLocation();
    }


    @OnClick({R.id.splash})
    public void onClick(View v) {

    }


}
