package dingshi.com.hibook;


import com.bugtags.library.Bugtags;
import com.pgyersdk.crash.PgyCrashManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * ØØ
 *
 * @author wangqi
 *         Created by apple on 2017/10/25.
 */

public class MyApplication extends TinkerApplication {
    private static MyApplication instance;

    private RefWatcher mRefWatcher;

    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "dingshi.com.hibook.MyApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);


    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            //初始化蒲公英
            PgyCrashManager.register(this);
            //初始化bugtags
            Bugtags.start("50d8b7dca2e6f3b5dcc1ecdce58b9e0d", this, Bugtags.BTGInvocationEventBubble);
            //初始化LeakCanary,LeakCanary.install(this);
            mRefWatcher = LeakCanary.install(this);
        }
    }

    //    @Override
//    public void onCreate() {
//        super.onCreate();
//        instance = this;
//        startService(new Intent(this, DownloadService.class));
//    }
    public static MyApplication getInstance() {
        return instance;
    }
}
