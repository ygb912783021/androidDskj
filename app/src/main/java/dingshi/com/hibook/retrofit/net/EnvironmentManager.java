package dingshi.com.hibook.retrofit.net;

import android.text.TextUtils;

import dingshi.com.hibook.utils.SpUtils;

/**
 * Created by : blank
 * Created on : 2018/5/11 at 17:53
 * Description: 后台环境切换工具
 */

public class EnvironmentManager {

    public static final String BASE_URL_DEV = "http://192.168.2.137/hellobook_api/";
    public static final String BASE_URL_RELEASE = "http://api.linkbooker.com/";
    private final String ENV_LABEL_RELEASE = "正式环境";
    private final String ENV_LABEL_DEV = "隆斌环境";
    private static EnvironmentManager instance = null;

    public static EnvironmentManager getInstance() {
        if (instance == null) {
            instance = new EnvironmentManager();
        }
        return instance;
    }


    public void switchEnvironment(boolean release) {

        if (release) {
            SpUtils.putBaseEnv(BASE_URL_RELEASE);
        } else {
            SpUtils.putBaseEnv(BASE_URL_DEV);

        }

    }

    /**
     * 获取环境名称
     *
     * @return
     */
    public String getEnvLabel() {
        if (!TextUtils.isEmpty(SpUtils.getBaseEnv())) {
            if (SpUtils.getBaseEnv().equals(BASE_URL_RELEASE))
                return ENV_LABEL_RELEASE;
            else {
                return ENV_LABEL_DEV;
            }
        }
        return ENV_LABEL_RELEASE;
    }

}
