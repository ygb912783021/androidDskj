package dingshi.com.hibook.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.yuyh.library.imgsel.bean.Image;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import dingshi.com.hibook.Constant;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.lib.LibList;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wangqi
 * @since 2018/1/30 上午11:09
 */

public class LibZxingActivity extends BaseActivity {

    @BindView(R.id.lib_zxing_pic)
    ImageView imgZxing;

    LibList.JsonDataBean libBean;
    Bitmap zxingBitmap;

    boolean isRally;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lib_zxing;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        libBean = (LibList.JsonDataBean) getIntent().getSerializableExtra("libBean");
        isRally = getIntent().getBooleanExtra("isRally", false);

        if (isRally) {
            requestActionBarStyle(true, "书友会二维码", "", R.drawable.invite_share);
        } else {
            requestActionBarStyle(true, "图书馆二维码", "", R.drawable.invite_share);
        }

        Flowable.create(new FlowableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(FlowableEmitter<Bitmap> e) throws Exception {
                Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                logoBitmap = QRCodeEncoder.syncEncodeQRCode(
                        Constant.getCatalogUrl(user.getJsonData().getUser_id(), libBean.getCatalog_id(), isRally ? 2 : 1, libBean.getBook_sum(), libBean.getBook_share_sum(), libBean.getUser_total()),
                        BGAQRCodeUtil.dp2px(LibZxingActivity.this, 150), Color.BLACK, Color.WHITE, logoBitmap);
                e.onNext(logoBitmap);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {
                        if (bitmap != null) {
                            zxingBitmap = bitmap;
                            imgZxing.setImageBitmap(bitmap);
                        } else {
                            showToast("生成中文二维码失败");
                        }
                    }
                });


    }

    @Override
    public void onRightClick() {
        super.onRightClick();
        showShareBitmapDialog(zxingBitmap);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        zxingBitmap.recycle();
    }
}
