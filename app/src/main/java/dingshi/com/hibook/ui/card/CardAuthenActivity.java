package dingshi.com.hibook.ui.card;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yuyh.library.imgsel.ISNav;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.ImageSelector;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;

/**
 * @author wangqi
 * @since 2017/12/20 上午11:31
 */

public class CardAuthenActivity extends BaseActivity {

    public final int REQUEST_CARD1_CODE = 0x01;
    public final int REQUEST_CARD2_CODE = 0x02;

    @BindView(R.id.card_authen_idcard_img1)
    ImageView idCard1;
    @BindView(R.id.card_authen_idcard_img2)
    ImageView idCard2;
    @BindView(R.id.card_authen_idcard_txt1)
    TextView txCard1;
    @BindView(R.id.card_authen_idcard_txt2)
    TextView txCard2;

    /**
     * 非必要,当是身份证时，该字段有值，1.正面,2.反面
     */
    private boolean isAbove;


    private String cardId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_card_authen;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "名片认证", "");
        cardId = getIntent().getStringExtra("card_id");


    }

    @OnClick({R.id.card_authen_idcard1, R.id.card_authen_idcard2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_authen_idcard1:
                isAbove = true;
                txCard1.setVisibility(View.GONE);
                ISNav.getInstance().toListActivity(this, ImageSelector.getSingleNoCropConfig(), REQUEST_CARD1_CODE);
                break;
            case R.id.card_authen_idcard2:
                isAbove = false;
                txCard2.setVisibility(View.GONE);
                ISNav.getInstance().toListActivity(this, ImageSelector.getSingleNoCropConfig(), REQUEST_CARD2_CODE);
                break;
            default:
        }

    }


    public void submit(File file) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("ident_apply") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {

            }

            @Override
            protected void onSuccess(Result response) {
            }
        };
        /**
         *   还需要设置两个图片
         */

        HashMap<String, String> map = new HashMap<>(2);
        map.put("card_id", cardId);
        //type 必要,证件类型 1：身份证，2：名片等职业证件
        map.put("type", "1");
        map.put("image_type", isAbove ? "1" : "2");
        map = AppSign.buildMap(map);

        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("image", file.getName(), photoRequestBody);

        Observable<Result> observable = NetUtils.getGsonRetrofit().identApply(map, photoPart);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        // 图片选择结果回调
        if (requestCode == REQUEST_CARD1_CODE) {
            List<String> pathList = data.getStringArrayListExtra("result");
            GlideUtils.load(this, pathList.get(0), idCard1);
            compass(pathList);
        }

        if (requestCode == REQUEST_CARD2_CODE) {
            List<String> pathList = data.getStringArrayListExtra("result");
            GlideUtils.load(this, pathList.get(0), idCard2);
            compass(pathList);
        }
    }


    public void compass(List<String> photos) {
        Flowable.just(photos)
                .observeOn(Schedulers.io())
                .map(new Function<List<String>, List<File>>() {
                    @Override
                    public List<File> apply(@NonNull List<String> list) throws Exception {
                        // 同步方法直接返回压缩后的文件
                        return Luban.with(CardAuthenActivity.this).load(list).get();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(@NonNull List<File> files) throws Exception {
                        submit(files.get(0));
                    }
                });
    }
}
