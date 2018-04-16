package dingshi.com.hibook.share;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import dingshi.com.hibook.base.BaseUmengActivity;
import dingshi.com.hibook.bean.Payment;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.SpUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


/**
 * @author wangqi
 * @since 2017/11/15 10:21
 */

public class EasyPayShare {

    public static final String ZFB_APPID = "2017101209259403";
    public static final String WEIXIN_APP_ID = "wx642fd79d7b199d92";
    public static final String WEIXIN_SECRET = "9c1a6f1794caa029a3f13b311f64eb1d";


    public static IWXAPI wxApi;


    private EasyPayShare() {
    }


    private static class InnerClass {
        static EasyPayShare Instance = new EasyPayShare();
    }

    public static EasyPayShare getInstance() {
        return InnerClass.Instance;
    }

    public void registWeixin(Context context, @NonNull String appId) {
        wxApi = WXAPIFactory.createWXAPI(context, appId, true);
        wxApi.registerApp(appId);
    }


    public void createWeixin(Context context, @NonNull String appId) {
        wxApi = WXAPIFactory.createWXAPI(context, appId);
    }


    public IWXAPI getWxApi() {
        return wxApi;
    }


    public void doShareWx(Context context, ShareParams wxShareParams, ShareCallBack callBack) {
        IWeixin share = new WxShare(context);
        share.share(wxShareParams, callBack);
    }


    public void doLoginWx(Context context, ShareCallBack callBack) {
        IWeixin share = new WxShare(context);
        share.login(callBack);
    }

    public void doPayWx(Context context, PayWeixin payWeixin, ShareCallBack callBack) {
        IWeixin share = new WxShare(context);
        share.pay(payWeixin, callBack);
    }


    public void doPayAli(String orderInfo, Activity activity, ShareCallBack callBack) {
        IAliPay aliPay = new AliShare(activity);
        aliPay.pay(orderInfo, callBack);
    }


    public void doLoginAli(final Activity mActivity, final OnBackListener backListener) {
        final AuthTask authTask = new AuthTask(mActivity);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://testapi.linkbooker.com/aliopen/signature?" + AppSign.buildString(new HashMap<String, String>()));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String len;
                    while ((len = br.readLine()) != null) {
                        sb.append(len);
                    }
                    Payment payment = new Gson().fromJson(sb.toString(), Payment.class);
                    Map<String, String> result = authTask.authV2(payment.getJsonData().getSign(), true);
                    AuthResult authResult = new AuthResult(result, false);

                    for (Map.Entry<String, String> map : result.entrySet()) {
                        Log.i("ali_login", map.getKey() + "---" + map.getValue());
                    }
                    Looper.prepare();
                    backListener.back(authResult);
                    Looper.loop();

                } catch (Exception e) {


                }
            }
        };

        ThreadPoolManager.getInstance().execute(runnable);
    }


    OnBackListener backListener;

    public void setOnBackListener(OnBackListener backListener) {
        this.backListener = backListener;
    }

    public interface OnBackListener {
        void back(AuthResult authResult);
    }


}
