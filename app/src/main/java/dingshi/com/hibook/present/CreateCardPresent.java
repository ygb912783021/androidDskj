package dingshi.com.hibook.present;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.widget.DatePicker;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import dingshi.com.hibook.action.ICreateCardView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.view.GenderView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author wangqi
 * @since 2017/12/19 16:31
 */

public class CreateCardPresent extends BasePresent<ICreateCardView, BaseActivity> {
    public CreateCardPresent(ICreateCardView view, BaseActivity activity) {
        super(view, activity);
    }

    /**
     * 名片公开
     */
    public String[] pros = {"对群友可见(上商务推荐)", "交换名片可见(私密安全)"};
    AlertDialog dialog;

    public void openCardType() {
        dialog = new AlertDialog.Builder(getActivity())
                .setTitle("私密")
                .setSingleChoiceItems(pros, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                getView().onCardType(pros[which], String.valueOf(which + 1));
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    /**
     * 打开性别
     */
    public void openGender() {
        GenderView view = new GenderView(getActivity());
        view.show();
        view.setOnGenderCallBackListner(new GenderView.GenderCallBack() {
            @Override
            public void back(String result) {
                if ("1".equals(result)) {
                    getView().onGender("男", result);
                } else if ("2".equals(result)) {
                    getView().onGender("女", result);
                }
            }
        });
    }


    /**
     * 获取时间
     */
    Calendar c;

    public void openBirth() {
        c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                getView().onBirth(DateFormat.format("yyyy-MM-dd", c).toString());

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


    public void submit(HashMap<String, String> map, boolean isEdit, String photo) {

        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("cardAdd") {
            @Override
            protected void onStart(Disposable d) {
                getView().start();
            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Result response) {
                getView().onSuccess(response);
            }
        };
        map = AppSign.buildMap(map);
        if (isEdit) {
            edit(map, httpRxObserver);
        } else {
            save(map, httpRxObserver, photo);
        }

    }


    public void save(HashMap<String, String> map, HttpRxObserver httpRxObserver, String photo) {
        File file = new File(photo);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("image", file.getName(), photoRequestBody);

        Observable<Result> observable = NetUtils.getGsonRetrofit().cardAdd(map, photoPart);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    public void edit(HashMap<String, String> map, HttpRxObserver httpRxObserver) {
        Observable<Result> observable = NetUtils.getGsonRetrofit().cardEdit(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 编辑状态下的上传名片的头像
     *
     * @param strPhoto
     * @param cardId
     */
    public void uploadAvatar(String strPhoto, String cardId) {

        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("cardAdd") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Result response) {
            }
        };
        HashMap<String, String> map = new HashMap<>();
        map.put("card_id", cardId);
        map = AppSign.buildMap(map);


        File file = new File(strPhoto);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("image", file.getName(), photoRequestBody);

        Observable<Result> observable = NetUtils.getGsonRetrofit().cardAvatar(map, photoPart);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
}
