package dingshi.com.hibook.present;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.util.HashMap;

import dingshi.com.hibook.action.ICreateLibView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.lib.LibCreate;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author wangqi
 * @since 2017/12/25 10:45
 */

public class CreateLibPresent extends BasePresent<ICreateLibView, BaseActivity> {
    public CreateLibPresent(ICreateLibView view, BaseActivity activity) {
        super(view, activity);
    }

    public String[] types = {"个人", "公司", "组织", "机构"};

    public String[] rallys = {"城市书友会", "大学书友会", "老乡书友会"};

    public String[] checks = {"不审核,自动加入", "加入需要的我审核"};

    public String[] psws = {"号码完全公开(谨慎泄漏隐私)", "同群不加密", "号码后8位加星(158********)"};
    AlertDialog dialog;

    /**
     * true创建书友会   false创建图书管
     *
     * @param isRally
     */
    public void onSelectType(final boolean isRally) {
        types = isRally ? rallys : types;
        dialog = new AlertDialog.Builder(getActivity())
                .setTitle("类别")
                .setSingleChoiceItems(types, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                getView().onSelectType(String.valueOf(which + (isRally ? 6 : 1)), types[which]);
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }


    public void onSelectCheck() {
        dialog = new AlertDialog.Builder(getActivity())
                .setTitle("入馆审核")
                .setSingleChoiceItems(checks, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                getView().onSelectCheck(String.valueOf(which + 1), checks[which]);
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    public void onSelectPsw() {
        dialog = new AlertDialog.Builder(getActivity())
                .setTitle("手机号加密")
                .setSingleChoiceItems(psws, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                getView().onSelectPsw(String.valueOf(which + 1), psws[which]);
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    /**
     * @param strType     类别
     * @param strName     名称
     * @param strCheck    入馆审核
     * @param strDescribe 描述
     * @param strPsw      手机号机密
     * @param strPhoto    头像
     */
    public void onSubmit(String strType, String strName, String strCheck, String strDescribe, String strPsw, String strPhoto, String catalogId, boolean isEdit) {

        if (TextUtils.isEmpty(strName)) {
            getView().onError("请输入名称");
            return;
        }
        if (TextUtils.isEmpty(strDescribe)) {
            getView().onError("请输入描述");
            return;
        }

        if (TextUtils.isEmpty(strPhoto) && !isEdit) {
            getView().onError("请添加图标");
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("type", strType);
        map.put("name", strName);
        map.put("is_check", strCheck);
        map.put("describe", strDescribe);
        map.put("is_encrypt", strPsw);
        map.put("catalog_id", catalogId);
        map = AppSign.buildMap(map);


        HttpRxObserver httpRxObserver = new HttpRxObserver<LibCreate>("libCreate") {
            @Override
            protected void onStart(Disposable d) {
                getView().start();
            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(LibCreate result) {
                getView().onSuccess(result);
            }
        };


        if (isEdit) {
            onEdit(httpRxObserver, map);
        } else {
            onCreate(httpRxObserver, map, strPhoto);
        }

    }

    private void onCreate(HttpRxObserver httpRxObserver, HashMap<String, String> map, String photo) {
        File file = new File(photo);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("image", file.getName(), photoRequestBody);
        Observable<LibCreate> observable = NetUtils.getGsonRetrofit().libCreate(map, photoPart);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    private void onEdit(HttpRxObserver httpRxObserver, HashMap<String, String> map) {
        Observable<LibCreate> observable = NetUtils.getGsonRetrofit().libEdit(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 更改图书馆头像
     *
     * @param strPhoto
     * @param catalogId
     */
    public void updateAvatar(String strPhoto, String catalogId) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("libAvatar") {
            @Override
            protected void onStart(Disposable d) {
                getView().start();
            }

            @Override
            protected void onError(ApiException e) {
                getView().onError(e.getMsg());
            }

            @Override
            protected void onSuccess(Result result) {
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalogId);
        map = AppSign.buildMap(map);

        File file = new File(strPhoto);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("image", file.getName(), photoRequestBody);

        Observable<Result> observable = NetUtils.getGsonRetrofit().libAvatar(map, photoPart);

        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
}
