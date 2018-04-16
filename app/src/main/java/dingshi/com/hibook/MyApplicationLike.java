package dingshi.com.hibook;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;

import dingshi.com.hibook.hx.DemoHelper;
import dingshi.com.hibook.share.EasyPayShare;
import dingshi.com.hibook.utils.SpUtils;
import dingshi.com.hibook.view.BookRefreshFooter;
import dingshi.com.hibook.view.BookRefreshHeader;

/**
 * @author wangqi
 * @since 2017/11/7 上午11:45
 */

public class MyApplicationLike extends DefaultApplicationLike {
    public static final String TAG = "Tinker.MyApplicationLike";

    public static String channel = "debug";
    public static String deviceToken = "debug";

    /**
     * 经纬度
     */
    public static String lat = "31.1888070000";
    public static String lng = "121.5313590000";

    /**
     * 设置全局的下拉刷新和上拉加载
     */
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new BookRefreshHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new BookRefreshFooter(context);
            }
        });
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        PlatformConfig.setWeixin("wx642fd79d7b199d92", "9c1a6f1794caa029a3f13b311f64eb1d");
//        PlatformConfig.setSinaWeibo("226579437", "daa75d1adfc493351f5562e7392796c8", "http://as.baidu.com/software/22332853.html");//http://sns.whalecloud.com
//        PlatformConfig.setQQZone("1106419560", "swXcHO0gURSNRvPW");
    }

    public MyApplicationLike(Application application, int tinkerFlags,
                             boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                             long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }


    private static RefWatcher sRefWatcher;


    @Override
    public void onCreate() {
        super.onCreate();


        Beta.autoInit = true;
        Beta.autoCheckUpgrade = true;
        Beta.smallIconId = R.drawable.logo_roud;
        //wifi下自动下载
//        Beta.autoDownloadOnWifi = true;
        Beta.canShowUpgradeActs.add(MainActivity.class);
//        设置自定义升级对话框UI布局
//        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        if (BuildConfig.DEBUG) {
            Bugly.init(getApplication(), Constant.BUGLY_DEBUG_APPID, true);
        } else {
            Bugly.init(getApplication(), Constant.BUGLY_RELEASE_APPID, false);
        }
        //初始化环信
        DemoHelper.getInstance().init(getApplication());
        //初始化全局异常捕获
//        Thread.setDefaultUncaughtExceptionHandler(new AppException(getApplication()));
        /*
          初始化LeakCanary
         */
//        sRefWatcher = LeakCanary.install(getApplication());


        /*
         * 自定义图片加载器
         */
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });


        /*
         * 初始化sp
         */
        SpUtils.init(getApplication());

        /*
         * 初始化微信
         */
        EasyPayShare.getInstance().registWeixin(getApplication(), Constant.WEIXIN_APP_ID);

        /*
         * 获取渠道号
         */
//        channel = ChannelUtils.getAppMetaData(getApplication(), "BUGLY_APP_CHANNEL");
        channel = BuildConfig.FLAVOR;
        deviceToken = BuildConfig.BUILD_TYPE;

        /*
         * 初始化友盟
         */

        if (BuildConfig.DEBUG) {
            Config.DEBUG = true;
            UMConfigure.setLogEnabled(true);
        } else {
            Config.DEBUG = false;
            UMConfigure.setLogEnabled(false);
        }

        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(getApplication());
        UMConfigure.init(getApplication(), UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setScenarioType(getApplication(), MobclickAgent.EScenarioType.E_UM_NORMAL);


    }

    public static RefWatcher getRefWatcher() {
        return sRefWatcher;
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker(this);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

}
