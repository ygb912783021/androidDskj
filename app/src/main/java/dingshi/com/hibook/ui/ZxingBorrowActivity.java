package dingshi.com.hibook.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.card.CardDetails;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.library.JoinLibActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.KefuUtils;
import dingshi.com.hibook.utils.strategy.CardStrategy;
import dingshi.com.hibook.utils.strategy.CaseStrategy;
import dingshi.com.hibook.utils.strategy.CatalogStrategy;
import dingshi.com.hibook.utils.strategy.HttpUrlStrategy;
import dingshi.com.hibook.utils.strategy.Strategy;
import dingshi.com.hibook.utils.strategy.StrategyHelper;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/28 上午9:44
 */


public class ZxingBorrowActivity extends BaseActivity implements QRCodeView.Delegate {
    @BindView(R.id.zxing_borrow_zxing)
    ZXingView zxingView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_zxing_borrow;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "扫码借书", "使用帮助");
        zxingView.setDelegate(this);
        zxingView.startSpot();


    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        judgeResult(result);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }


    /**
     * @param url
     */
    public void judgeResult(String url) {

        Log.i("url", url);

        Strategy strategy = new Strategy();

        strategy.setStrategy(new HttpUrlStrategy());
        String http = strategy.pattern(url);

        if ("".equals(http)) {
            jumpBookHouse();
        } else {
            strategy.setStrategy(new CardStrategy());
            String card = strategy.pattern(url);
            if (!"".equals(card)) {
                sweepCard(card);
                return;
            }
            strategy.setStrategy(new CaseStrategy());
            String bookCase = strategy.pattern(url);
            if (!"".equals(bookCase)) {
                jumpBookCase(bookCase);
                return;
            }
            strategy.setStrategy(new CatalogStrategy());
            String catalog = strategy.pattern(url);
            if (!"".equals(catalog)) {
                jumpCatalog(catalog);
                return;
            }
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra("url", http);
            startActivity(intent);
        }
    }


    @Override
    public void onRightClick() {
        super.onRightClick();
        showTip();
    }


    public void showTip() {
        dialog = new AlertDialog.Builder(this).setTitle("一键扫码，快速开启").setMessage("在共享图书的自动借书机上都有对应的二维码，只需用手机扫码，便可快速借书")
                .create();

        dialog.show();
    }

    /**
     * 跳转书房
     */
    AlertDialog dialog;

    public void jumpBookHouse() {
        dialog = new AlertDialog.Builder(this)
                .setMessage("当前书库没有这本书哦,需要添加的话可以联系客服")
                .setNegativeButton("联系客服", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        KefuUtils.jump(getApplication());
                        finish();
                    }
                }).create();

        dialog.show();

    }


    /**
     * 跳转书柜
     *
     * @param sn
     */
    public void jumpBookCase(String sn) {
        Intent intent = new Intent(this, Case2BookActivity.class);
        intent.putExtra("serial_number", sn);
        startActivity(intent);
        finish();
    }


    /**
     * 跳转书柜
     *
     * @param catalog
     */
    public void jumpCatalog(String catalog) {
        String arr[] = catalog.split("=");
        Intent intent = new Intent();
        if ("1".equals(arr[1])) {
            intent.setClass(this, JoinLibActivity.class);
        } else if ("2".equals(arr[1])) {
            intent.setClass(this, RallyDetailsActivity.class);
        }

        intent.putExtra("catalog_id", arr[0]);
        startActivity(intent);
        finish();
    }


    /**
     * 交换名片
     *
     * @param userId
     */
    public void sweepCard(String userId) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<CardDetails>("cardApply") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                showToast(e.getMsg());
                zxingView.startSpot();
            }

            @Override
            protected void onSuccess(CardDetails response) {
                showToast("已提交申请交换名片");
            }
        };
        /**
         *   还需要设置两个图片
         */
        HashMap<String, String> map = new HashMap<>(2);
        map.put("uid", userId);
        map = AppSign.buildMap(map);
        Observable<CardDetails> observable = NetUtils.getGsonRetrofit().cardApply(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    @Override
    protected void onStart() {
        super.onStart();
        zxingView.startCamera();
        zxingView.showScanRect();
    }

    @Override
    protected void onStop() {
        zxingView.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        zxingView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
