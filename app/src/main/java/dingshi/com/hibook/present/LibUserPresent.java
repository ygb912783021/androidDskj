package dingshi.com.hibook.present;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.HashMap;

import dingshi.com.hibook.action.ILibUserView;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.lib.LibIntro;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2018/3/1 10:38
 */

public class LibUserPresent extends BasePresent<ILibUserView, BaseFragment> {
    public LibUserPresent(ILibUserView view, BaseFragment activity) {
        super(view, activity);
    }

    /**
     * 获取用户列表
     *
     * @param keyWorlds
     * @param catalogId
     * @param page
     */
    public void getUserList(String keyWorlds, String catalogId, int page) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<LibIntro>("libUser") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(LibIntro libIntro) {
                getView().onUserList(libIntro);
            }
        };

        HashMap<String, String> map = new HashMap<>(3);
        map.put("key_word", keyWorlds);
        map.put("catalog_id", catalogId);
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);
        Observable<LibIntro> user = NetUtils.getGsonRetrofit().libUser(map);
        HttpRxObservable.getObservable(user, getActivity(), FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 踢出目录
     *
     * @param catalogId
     * @param uid
     */
    public void deleteUser(String catalogId, String uid) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("libKicked") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(Result libIntro) {
                getView().onDeleteUser();
            }
        };

        HashMap<String, String> map = new HashMap<>(3);
        map.put("catalog_id", catalogId);
        map.put("uid", uid);
        map = AppSign.buildMap(map);
        Observable<Result> user = NetUtils.getGsonRetrofit().libKicked(map);
        HttpRxObservable.getObservable(user, getActivity(), FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }

    private AlertDialog dialog;

    public void deleteUserShowDialog(final String catalogId, final String uid) {
        dialog = new AlertDialog.Builder(getActivity().mActivity).setTitle("提醒")
                .setMessage("确定将此人踢出图书目录?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(catalogId, uid);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
}
