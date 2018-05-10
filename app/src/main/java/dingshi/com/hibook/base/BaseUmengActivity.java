package dingshi.com.hibook.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.Map;

import dingshi.com.hibook.R;
import dingshi.com.hibook.utils.CheckWecheatUtil;
import dingshi.com.hibook.view.FuckDialog;

/**
 * @author wangqi
 * @since 2018/1/15 18:25
 */

public class BaseUmengActivity extends BaseRxActivity {
    private static final String TAG = "BaseUmengActivity";

    UMWeb web;

    public void share(String title, String content, String url, SHARE_MEDIA platform) {
        //初始化分享组件
        web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(new UMImage(this, R.drawable.logo));
        web.setDescription(content);
        new ShareAction(BaseUmengActivity.this)
                .withText(getResources().getString(R.string.app_name))
                .withMedia(web)
                .setPlatform(platform)
                .setCallback(shareListener).share();
    }


    public void shareBitmap(Bitmap bitmap, SHARE_MEDIA platform) {
        UMImage imagelocal = new UMImage(this, bitmap);
        imagelocal.setThumb(new UMImage(this, bitmap));
        new ShareAction(BaseUmengActivity.this).withMedia(imagelocal)
                .withText(getResources().getString(R.string.app_name))
                .setPlatform(platform)
                .setCallback(shareListener).share();
    }

    /**
     * 显示分享dialog
     *
     * @param title
     * @param describe
     * @param url
     */
    FuckDialog shareDialog;

    /**
     * 分享内容
     *
     * @param title
     * @param describe
     * @param url
     */
    public void showShareDialog(final String title, final String describe, final String url) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_share, null, false);
        shareDialog = new FuckDialog(this).addView(view).builder();
        shareDialog.show();
        view.findViewById(R.id.dialog_share_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(title, describe, url, SHARE_MEDIA.WEIXIN.toSnsPlatform().mPlatform);
                shareDialog.dissmis();
            }
        });
        view.findViewById(R.id.dialog_share_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(title, describe, url, SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform().mPlatform);
                shareDialog.dissmis();
            }
        });

    }

    /***
     * 分享图片
     * @param bitmap
     */
    public void showShareBitmapDialog(final Bitmap bitmap) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_share, null, false);
        shareDialog = new FuckDialog(this).addView(view).builder();
        shareDialog.show();
        view.findViewById(R.id.dialog_share_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBitmap(bitmap, SHARE_MEDIA.WEIXIN.toSnsPlatform().mPlatform);
                shareDialog.dissmis();
            }
        });
        view.findViewById(R.id.dialog_share_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBitmap(bitmap, SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform().mPlatform);
                shareDialog.dissmis();
            }
        });

    }


    public void loginWx() {
//        if (!EasyPayShare.wxApi.isWXAppInstalled()) {
        if (!CheckWecheatUtil.checkWecheat(this)) {
            Toast.makeText(BaseUmengActivity.this, "未安装微信", Toast.LENGTH_LONG).show();
            return;
        }
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override//SHARE_MEDIA  可以判断授权的第三方是什么
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override//授权成功回调
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            loginSuccess(map);
        }

        @Override//授权失败回调
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//            Toast.makeText(BaseUmengActivity.this, "未安装客户端", Toast.LENGTH_LONG).show();

        }

        @Override//取消授权回调
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Toast.makeText(BaseUmengActivity.this, "取消登录", Toast.LENGTH_LONG).show();
        }
    };


    public void loginSuccess(Map<String, String> map) {

        /**
         * openid :
         * platform_type : 3
         * user_id : 491823389864890376
         * sex : 0
         * nick_name : 15055407294
         * avatar :
         * balance : 0  余额
         * mobile : 15055407294
         * birthday: "2017-11-16",
         * province :
         * city :
         * cert_status :  押金 没0  交过1
         * disable : 0
         * signable : 1
         * created_at : 2017-11-09 14:05:16
         * token : a15da4b74b8c15d8220e41c5323a6ef886a5a814
         * foregift : 用户押金的钱是多少
         * read_num   读书量
         * book_num   藏书量
         * balance_refund_status  押金的状态  0、无状态  1、审核中
         */


//        for (Map.Entry entry : map.entrySet()) {
//            Log.d(TAG, entry.getKey() + "---" + entry.getValue());
//        }
    }

    private String get(Map<String, String> dataSrc, String key) {
        if (TextUtils.isEmpty(key)) {
            return "";
        }
        if (dataSrc.containsKey(key)) {
            return dataSrc.get(key);
        }
        return "";
    }


    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(BaseUmengActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(BaseUmengActivity.this, "未安装客户端", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(BaseUmengActivity.this, "分享取消", Toast.LENGTH_LONG).show();

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(BaseUmengActivity.this).onActivityResult(requestCode, resultCode, data);//完成回调
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(BaseUmengActivity.this).release();
    }


    @Override
    protected void onPause() {
//        MobclickAgent.onPause(this);
        super.onPause();

    }

    @Override
    protected void onResume() {
//        MobclickAgent.onResume(this);
        super.onResume();

    }
}
