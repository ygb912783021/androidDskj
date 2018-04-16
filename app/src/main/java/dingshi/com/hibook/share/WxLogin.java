package dingshi.com.hibook.share;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author wangqi
 * @since 2017/11/18 14:25
 */

public class WxLogin {

    private static WxLogin login = new WxLogin();

    public static WxLogin getInstance() {
        return login;
    }

    public void login(String code, WeixinLoginCallBack weixinLoginCallBack) {
        this.weixinLoginCallBack = weixinLoginCallBack;
        ThreadPoolManager.getInstance().execute(new MyRunnable(code));
    }


    private class MyRunnable implements Runnable {
        String code;
        String access_token;
        String expires_in;
        String refresh_token;
        String openid;
        String scope;
        String unionid;

        public MyRunnable(String code) {
            this.code = code;
        }

        @Override
        public void run() {
            try {
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                        "appid=" + EasyPayShare.WEIXIN_APP_ID +
                        "&secret=" + EasyPayShare.WEIXIN_SECRET +
                        "&code=" + code +
                        "&grant_type=authorization_code";
                //获取token
                String json = HttpUtils.getHtml(url);
                Log.i("weixin", "获取token:" + json);

                //验证成功
                if (validateSuccess(json)) {
                    JSONObject obj = new JSONObject(json);
                    access_token = obj.getString("access_token");
                    expires_in = obj.getString("expires_in");
                    refresh_token = obj.getString("refresh_token");
                    openid = obj.getString("openid");
                    scope = obj.getString("scope");
                    unionid = obj.getString("unionid");

                    //请求获取用户信息
                    url = "https://api.weixin.qq.com/sns/userinfo?" +
                            "access_token=" + access_token +
                            "&openid=" + openid;

                    json = HttpUtils.getHtml(url);

                    Log.i("weixin", "info:" + json);
                    weixinLoginCallBack.onSuccess(json);
                } else {
                    weixinLoginCallBack.onError("WEIXIN APPID OR SECRET ERROR");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean validateSuccess(String response) {
        String errFlag = "errmsg";
        return (errFlag.contains(response) && !"ok".equals(response))
                || (!"errcode".contains(response) && !errFlag.contains(response));
    }


    public WeixinLoginCallBack weixinLoginCallBack;

    public interface WeixinLoginCallBack {
        void onSuccess(String result);

        void onError(String error);
    }
}
