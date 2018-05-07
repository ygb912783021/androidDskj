package dingshi.com.hibook.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

/**
 * Created by : blank
 * Created on : 2018/5/7 at 12:01
 * Description: 当手机上已经安装微信，而友盟检测方法会检测失败，但是登录是可以使用的，所以使用自己的检测方法，
 */

public class CheckWecheatUtil {
    private static final String TAG = "CheckWecheatUtil";
    /**
     * 微信包名，用于微信是否安装检测
     */
    private static final String WECHEAT_PACKAGENAME = "com.tencent.mm";

    public static boolean checkWecheat(Context context) {

        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                Log.d(TAG, "onClick package name ::: " + pn);
                if (pn.equals(WECHEAT_PACKAGENAME))
                    return true;
            }
        }
        return false;
    }
}
