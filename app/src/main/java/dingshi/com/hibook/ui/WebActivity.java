package dingshi.com.hibook.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;

/**
 * @author wangqi
 * @since 2017/12/28 上午11:38
 */
public class WebActivity extends BaseActivity {
    @BindView(R.id.web_error)
    LinearLayout errorLayout;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.web_progress)
    ProgressBar progressBar;

    WebSettings setting;
    String url,share_title,share_content;


    boolean flag = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "", "",R.drawable.invite_share);

        url = getIntent().getStringExtra("url");
        share_title = getIntent().getStringExtra("share_title");
        share_content = getIntent().getStringExtra("share_content");

        initWeb();
        initSetting();

        webView.loadUrl(url);
    }

    @Override
    public void onRightClick() {
        super.onRightClick();
        showShareDialog(share_title,share_content,url);
    }

    private void initWeb() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (progressBar != null) {
                    progressBar.setProgress(newProgress);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (webView == null || progressBar == null) {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                flag = false;
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                flag = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                requestActionBarStyle(true, view.getTitle(), "");

                if (webView == null || progressBar == null) {
                    return;
                }

                if (flag) {
                    requestActionBarStyle(true, "加载失败", "");
                    webView.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                } else {
                    requestActionBarStyle(true, view.getTitle(), "",R.drawable.invite_share);
                    webView.setVisibility(View.VISIBLE);

                }
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void initSetting() {
        setting = webView.getSettings();
        // 设置允许JS弹窗
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        //防止中文乱码
        setting.setDefaultTextEncodingName("UTF-8");
        //设置webview的缓存
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setting.setAllowContentAccess(true);
        setting.setAllowFileAccess(true);
        setting.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView.setWebChromeClient(null);
        }

    }
}
