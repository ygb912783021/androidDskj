package dingshi.com.hibook.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yuyh.library.imgsel.ISNav;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookList;
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
 * @since 2017/12/18 下午12:13
 */

public class CreateBookActivity extends BaseActivity {

    private static final int REQUEST_CAMERA_CODE = 0x9429;
    @BindView(R.id.create_book_isbn)
    EditText editIsbn;
    @BindView(R.id.create_book_book)
    EditText editBook;
    @BindView(R.id.create_book_cover)
    ImageView imgCover;
    @BindView(R.id.create_book_author)
    EditText editAuthor;
    @BindView(R.id.create_book_translator)
    EditText editTrans;
    @BindView(R.id.create_book_concern)
    EditText editConcern;
    @BindView(R.id.create_book_concern_time)
    TextView editConcernTime;
    @BindView(R.id.create_book_page)
    EditText editPage;
    @BindView(R.id.create_book_price)
    EditText editPrice;
    @BindView(R.id.create_book_content)
    EditText editContent;


    String strIsbn;
    String strBook;
    String strAuthor;
    String strTrans;
    String strConcern;
    String strConcernTime;
    String strPage;
    String strPrice;
    String strContent;


    List<File> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_creat_book;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "创建图书", "完成");
    }

    @Override
    public void onRightClick() {
        super.onRightClick();
        strIsbn = editIsbn.getText().toString();
        strBook = editBook.getText().toString();
        strAuthor = editAuthor.getText().toString();
        strTrans = editTrans.getText().toString();
        strConcern = editConcern.getText().toString();
//        strConcernTime = editConcernTime.getText().toString();
        strPage = editPage.getText().toString();
        strPrice = editPrice.getText().toString();
        strContent = editContent.getText().toString();

        if (list.size() == 0) {
            showToast("请选择图书封面");
            return;
        }
        if ("".equals(strIsbn) || "".equals(strBook) || "".equals(strAuthor)
                || "".equals(strTrans) || "".equals(strConcern)
                || "".equals(strConcernTime) || "".equals(strPage) || "".equals(strPrice)
                || "".equals(strContent)) {
            showToast("缺少必填项");
            return;
        }
        createBook();
    }


    @OnClick({R.id.create_book_cover, R.id.create_book_concern_time})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_book_cover:
                ISNav.getInstance().toListActivity(this, ImageSelector.getSingleNoCropConfig(), REQUEST_CAMERA_CODE);
                break;
            case R.id.create_book_concern_time:
                getTime();
                break;
            default:
        }

    }

    public void getTime() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                strConcernTime = DateFormat.format("yyyy-MM-dd", c).toString();
                editConcernTime.setText(strConcernTime);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


    public void compass(List<String> pathList) {
        Flowable.just(pathList)
                .observeOn(Schedulers.io())
                .map(new Function<List<String>, List<File>>() {
                    @Override
                    public List<File> apply(@NonNull List<String> list) throws Exception {
                        // 同步方法直接返回压缩后的文件
                        return Luban.with(CreateBookActivity.this).ignoreBy(100).load(list).get();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(List<File> files) throws Exception {
                        list.clear();
                        list.addAll(files);
                    }
                });
    }


    /**
     * 请求网络
     */
    public void createBook() {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", strIsbn);
        map.put("name", strBook);
        map.put("author", strAuthor);
        map.put("translator", strTrans);
        map.put("press", strConcern);
        map.put("publish_time", strConcernTime);
        map.put("summary", strContent);
        map.put("price", strPrice);
        map.put("page_number", strPage);
        map.put("book_type_id", "0");

        map = AppSign.buildMap(map);
        //构建头像
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), list.get(0));
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("image", list.get(0).getName(), photoRequestBody);

        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("avatar") {
            @Override
            protected void onStart(Disposable d) {
                showProgressDialog("请求中...", true);
            }

            @Override
            protected void onError(ApiException e) {
                showToast(e.getMsg());
                dismissProgressDialog();
            }

            @Override
            protected void onSuccess(Result u) {
                showToast("上传成功");
                finish();
                dismissProgressDialog();
                //通知书房更新
                EventBus.getDefault().post(new BookList());
            }
        };
        Observable<Result> observable = NetUtils.getGsonRetrofit().createBook(map, photoPart);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            GlideUtils.load(this, pathList.get(0), imgCover);
            compass(pathList);
        }
    }
}
