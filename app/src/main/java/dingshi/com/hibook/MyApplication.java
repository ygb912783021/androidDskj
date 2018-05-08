package dingshi.com.hibook;

import com.pgyersdk.crash.PgyCrashManager;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**ØØ
 * @author wangqi
 *         Created by apple on 2017/10/25.
 */

public class MyApplication extends TinkerApplication {

    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "dingshi.com.hibook.MyApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);

        initPgyer();
    }

    private void initPgyer() {
        if (BuildConfig.BUILD_TYPE.equals("debug")){
            PgyCrashManager.register(this);
        }
    }

}
