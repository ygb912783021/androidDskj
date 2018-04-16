package dingshi.com.hibook.apshare;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.BaseReq;
import com.alipay.share.sdk.openapi.BaseResp;
import com.alipay.share.sdk.openapi.IAPAPIEventHandler;
import com.alipay.share.sdk.openapi.IAPApi;

import dingshi.com.hibook.Constant;

/**
 * @author wangqi
 * @since 2017/11/8 上午9:31
 * <p>
 * 支付宝分享回调
 */


public class ShareEntryActivity extends Activity implements IAPAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //创建工具对象实例，此处的APPID为上文提到的，申请应用生效后，在应用详情页中可以查到的支付宝应用唯一标识
        IAPApi api = APAPIFactory.createZFBApi(getApplicationContext(), Constant.ZFB_APPID, false);
        Intent intent = getIntent();
        //通过调用工具实例提供的handleIntent方法，绑定消息处理对象实例，
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Toast.makeText(this, "Result Code:" + baseResp.errCode+baseResp.errStr, Toast.LENGTH_LONG).show();
    }
}
