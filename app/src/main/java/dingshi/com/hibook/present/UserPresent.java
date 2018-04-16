package dingshi.com.hibook.present;

import android.app.DatePickerDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import dingshi.com.hibook.R;
import dingshi.com.hibook.action.IUserView;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.UserActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.SpUtils;
import dingshi.com.hibook.view.FuckDialog;
import dingshi.com.hibook.view.GenderView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.R.attr.path;

/**
 * @author wangqi
 * @since 2017/11/9 17:55
 */

public class UserPresent extends BasePresent<IUserView, UserActivity> {

    public static final String BIRTHDAY = "birthday";
    public static final String SEX = "sex";
    public static final String NICKNAME = "nick_name";


    User user;

    public UserPresent(IUserView view, UserActivity activity, User user) {
        super(view, activity);
        this.user = user;
    }


    /**
     * 设置生日
     */
    public void born() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                updateUser(BIRTHDAY, DateFormat.format("yyyy-MM-dd", c).toString());
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


    /**
     * 设置性别
     */
    public void gender() {
        GenderView view = new GenderView(getActivity());
        view.show();
        view.setOnGenderCallBackListner(new GenderView.GenderCallBack() {
            @Override
            public void back(String result) {
                if (!"0".equals(result)) {
                    updateUser(SEX, result);
                }
            }
        });
    }

    /**
     * 设置昵称
     */
    public void nick() {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.view_dialog_nick, null, false);
        final FuckDialog dialog = new FuckDialog(getActivity())
                .addView(view)
                .builder();
        dialog.show();

        final EditText content = view.findViewById(R.id.nick_content);

        view.findViewById(R.id.nick_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = content.getText().toString().trim();
                if ("".equals(c)) {
                    getActivity().showToast("昵称设置不能为空");
                } else {
                    dialog.dissmis();
                    updateUser(NICKNAME, c);
                }

            }
        });
    }


    /**
     * 设置图片
     *
     * @param avatar
     */
    public void avatar(String avatar) {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("user_id", String.valueOf(user.getJsonData().getUser_id()));
        map = AppSign.buildMap(map);
        //构建头像
        File file = new File(avatar);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("image", file.getName(), photoRequestBody);

        HttpRxObserver httpRxObserver = new HttpRxObserver<User>("avatar") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
                getView().error(e.getMsg());
            }

            @Override
            protected void onSuccess(User u) {
                if (u.getJsonData() != null) {
                    Log.i("avatar", user.toString());
                    user.getJsonData().setAvatar(u.getJsonData().getAvatar());
                    getView().updateAvatar(u);
                    SpUtils.putUser(user);
                    Log.i("avatar", user.toString());
                    Log.i("avatar", SpUtils.getUser().toString());
                }
            }
        };
        Observable<User> observable = NetUtils.getGsonRetrofit().updateAvatar(map, photoPart);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);


    }


    /**
     * 用户更新
     *
     * @param key
     * @param value
     */
    private void updateUser(final String key, String value) {
        HashMap<String, String> map = new HashMap<>(2);
        map.put(key, value);
        map.put("user_id", String.valueOf(user.getJsonData().getUser_id()));
        map = AppSign.buildMap(map);

        HttpRxObserver httpRxObserver = new HttpRxObserver<User>("update") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
                getView().error(e.getMsg());
            }

            @Override
            protected void onSuccess(User u) {
                if (u.getJsonData() != null) {
                    backData(key, u);
                }
            }
        };

        Observable<User> observable = NetUtils.getGsonRetrofit().update(map);
        HttpRxObservable.getObservable(observable, getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    /**
     * @param key
     * @param u
     */
    private void backData(String key, User u) {
        Log.i("backData", key);
        User.JsonDataBean bean = u.getJsonData();
        switch (key) {
            case BIRTHDAY:
                getView().updateBorn(bean.getBirthday());
                user.getJsonData().setBirthday(bean.getBirthday());
                break;
            case SEX:
                if (bean.getSex() == 1) {
                    getView().updateGender("男");
                    user.getJsonData().setSex(1);
                } else if (bean.getSex() == 2) {
                    getView().updateGender("女");
                    user.getJsonData().setSex(2);
                }
                break;
            case NICKNAME:
                getView().updateNick(bean.getNick_name());
                user.getJsonData().setNick_name(bean.getNick_name());
                break;
            default:
        }

        Log.i("user", user.toString());
        /*
           给缓存重新设置数据
         */
        SpUtils.putUser(user);

        Log.i("user", SpUtils.getUser().toString());

    }

}
