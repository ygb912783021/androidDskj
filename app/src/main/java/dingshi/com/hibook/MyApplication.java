package dingshi.com.hibook;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.readbook.MApplication;
import com.example.administrator.readbook.service.DownloadService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;

import dingshi.com.hibook.view.BookRefreshFooter;
import dingshi.com.hibook.view.BookRefreshHeader;

/**
 * @author wangqi
 *         Created by apple on 2017/10/25.
 */

public class MyApplication extends TinkerApplication {
    private static MyApplication instance;
    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "dingshi.com.hibook.MyApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        startService(new Intent(this, DownloadService.class));
    }
    public static MyApplication getInstance() {
        return instance;
    }
}
