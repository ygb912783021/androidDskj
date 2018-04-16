package dingshi.com.hibook.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Toast;

import dingshi.com.hibook.R;
import dingshi.com.hibook.share.EasyPayShare;
import dingshi.com.hibook.share.IWeixin;
import dingshi.com.hibook.share.ShareCallBack;
import dingshi.com.hibook.share.ShareParams;

/**
 * @author wangqi
 * @since 2018/1/5 16:55
 */

public class ShareDialog extends BottomSheetDialog implements View.OnClickListener {
    Context context;

    public ShareDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }


    public ShareDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        initView(context);
    }

    protected ShareDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }


    private void initView(Context context) {
        this.context = context;
        setContentView(R.layout.view_dialog_share);
        findViewById(R.id.dialog_share_wx).setOnClickListener(this);
        findViewById(R.id.dialog_share_friend).setOnClickListener(this);
    }

    private String tilte;
    private String describe;
    private String webUrl;

    public void setContent(String tilte, String describe, String webUrl) {
        this.tilte = tilte;
        this.describe = describe;
        this.webUrl = webUrl;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_share_wx:
                wx();
                break;
            case R.id.dialog_share_friend:
                friend();
                break;
            default:
        }
        dismiss();
    }


    private void wx() {
        ShareParams params = new ShareParams.Build()
                .withTitle(tilte)
                .withDescribe(describe)
                .withUrl(webUrl)
                .withShareType(IWeixin.WXSceneSession)
                .build();
        EasyPayShare.getInstance().doShareWx(getContext(), params, new ShareCallBack() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled(String result) {
                Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String result) {
                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void friend() {
        ShareParams params = new ShareParams.Build()
                .withTitle(tilte)
                .withDescribe(describe)
                .withUrl(webUrl)
                .withShareType(IWeixin.WXSceneTimeline)
                .build();
        EasyPayShare.getInstance().doShareWx(getContext(), params, new ShareCallBack() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled(String result) {
                Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String result) {
                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
