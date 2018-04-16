package dingshi.com.hibook.present;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import dingshi.com.hibook.R;
import dingshi.com.hibook.action.ILibManagerView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.eventbus.LibRefreshEvent;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.EmailUtils;
import dingshi.com.hibook.view.FuckDialog;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2018/3/1 16:29
 */

public class LibManagerPresent extends BasePresent<ILibManagerView, BaseActivity> {
    public LibManagerPresent(ILibManagerView view, BaseActivity activity) {
        super(view, activity);
    }

    /**
     * 删除图书馆
     *
     * @param catalogId
     */

    public void deleteLib(final String catalogId) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("确认删除图书馆？")
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLibs(catalogId);
                        dialog.dismiss();
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }


    private void deleteLibs(String catalogId) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("libDelete") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError("删除失败");
            }

            @Override
            protected void onSuccess(Result response) {
                getView().onDeleteLib();
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalogId);
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().libDelete(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 导出名单
     */
    public void exportTable(final String catalog_id) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.view_dialog_nick, null, false);
        final FuckDialog dialog = new FuckDialog(getActivity())
                .addView(view)
                .builder();
        dialog.show();

        final EditText content = view.findViewById(R.id.nick_content);
        content.setHint("请输入邮箱地址");

        view.findViewById(R.id.nick_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = content.getText().toString().trim();
                if ("".equals(c)) {
                    getActivity().showToast("邮箱不能为空");
                } else if (!EmailUtils.isEmail(c)) {
                    getActivity().showToast("请输入正确的邮箱");
                } else {
                    dialog.dissmis();
                    exportTable(c, catalog_id);
                }

            }
        });
    }

    public void exportTable(String email, String catalog_id) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("exportUser") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                getView().onError("导出失败");
            }

            @Override
            protected void onSuccess(Result response) {
                getView().onExport();
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("catalog_id", catalog_id);
        map.put("email", email);
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().exportUser(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


}
