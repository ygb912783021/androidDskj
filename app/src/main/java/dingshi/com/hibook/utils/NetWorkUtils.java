package dingshi.com.hibook.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import dingshi.com.hibook.MyApplication;

/**
 * Created by : blank
 * Created on : 2018/5/8 at 15:28
 * Description:
 */

public class NetWorkUtils {
    /**
     * 检查网络的
     * @param context
     * @return
     */
    public static boolean hasInternet(Context context) {
        boolean flag;
        if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null)
            flag = true;
        else
            flag = false;
        return flag;
    }
}
