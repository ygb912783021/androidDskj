package dingshi.com.hibook.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * @author wangqi
 * @since 2017/11/18 15:10
 */


public class EasyWxEntryActivity extends Activity implements IWXAPIEventHandler {

    public static final int KEY_SHARE = 0x9429;
    public static final int KEY_LOGIN = 0x1129;

    ShareParams wxShareParams;
    ResultReceiver listener;
    int type;
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasyPayShare.getInstance().getWxApi().handleIntent(getIntent(), this);

        // 判断是否安装了微信客户端
        if (!EasyPayShare.getInstance().getWxApi().isWXAppInstalled()) {
            Toast.makeText(this, "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }



        bundle = getIntent().getExtras();
        type = bundle.getInt(WxShare.KEY_TYPE);
        listener = bundle.getParcelable(WxShare.KEY_SHARE_LISTENER);
        if (type == KEY_SHARE) {
            wxShareParams = bundle.getParcelable(WxShare.KEY_SHARE_PARAMS);
            share();
        } else if (type == KEY_LOGIN) {
            login();
        }

    }


    private void login() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "app_wechat";
        EasyPayShare.getInstance().getWxApi().sendReq(req);
    }


    /**
     * 分享
     */
    private void share() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = wxShareParams.webUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = wxShareParams.title;
        msg.description = wxShareParams.describe;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = wxShareParams.shareType;
        EasyPayShare.getInstance().getWxApi().sendReq(req);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        EasyPayShare.getInstance().getWxApi().handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp resp) {

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (type == KEY_SHARE) {
                    listener.send(IWeixin.RESULT_SUCCESS, bundle);
                    finish();
                } else if (type == KEY_LOGIN) {
                    WxLogin.getInstance().login(((SendAuth.Resp) resp).code, new WxLogin.WeixinLoginCallBack() {
                        @Override
                        public void onSuccess(String result) {
                            Looper.prepare();
                            bundle.putString("result", result);
                            listener.send(IWeixin.RESULT_SUCCESS, bundle);
                            finish();
                            Looper.loop();
                        }

                        @Override
                        public void onError(String error) {
                            Looper.prepare();
                            listener.send(IWeixin.RESULT_FAIL, bundle);
                            finish();
                            Looper.loop();
                        }
                    });
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                listener.send(IWeixin.RESULT_CANCELD, bundle);
                finish();
                break;
            default:
                listener.send(IWeixin.RESULT_FAIL, bundle);
                finish();
                break;
        }
    }

}
