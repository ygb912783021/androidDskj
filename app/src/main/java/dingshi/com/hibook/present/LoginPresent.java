package dingshi.com.hibook.present;

import android.util.Log;
import android.widget.Toast;

import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.HashMap;

import dingshi.com.hibook.action.ILoginView;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.base.BasePresent;
import dingshi.com.hibook.bean.Captcha;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.bean.WexinInfo;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.share.AuthResult;
import dingshi.com.hibook.share.EasyPayShare;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.PhoneUtils;
import dingshi.com.hibook.utils.SpUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/11/9 下午1:48
 */


public class LoginPresent extends BasePresent<ILoginView, BaseFragment> {

    public LoginPresent(ILoginView view, BaseFragment activity) {
        super(view, activity);
    }

    /**
     * 登录
     * <p>
     * 0.手机,1.支付宝,2.微信
     *
     * @param mobile   手机号
     * @param password 密码
     */
    public void login(String mobile, String password, boolean isCaptchLogin) {
        if (!PhoneUtils.isPhoneNumber(mobile)) {
            getView().error("请输入正确的手机号");
            return;
        }

        if (password.length() == 0 && !isCaptchLogin) {
            getView().error("请输入密码");
            return;
        }
        HttpRxObserver httpRxObserver = new HttpRxObserver<User>("login") {
            @Override
            protected void onStart(Disposable d) {
                getView().start();
            }

            @Override
            protected void onError(ApiException e) {
//                getView().error(e.getMsg());
                getView().error("登录失败");
            }

            @Override
            protected void onSuccess(User user) {
                if (user.getJsonData() != null) {
                    //登录成功后，将用户信息存储在本地
                    SpUtils.putUser(user);
                    getView().success();
                    Log.i("user_id=",user.getJsonData().getUser_id().toString());
                } else {
                    getView().error("密码错误");
                }
            }
        };


        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);

        if (isCaptchLogin) {
            map.put("captcha", password);
            map.put("signin_type", "1");
        } else {
            map.put("password", AppSign.getPassword(password));
            map.put("signin_type", "0");
        }


        map = AppSign.buildMap(map);

        Observable<User> user = NetUtils.getGsonRetrofit().login(map);

        HttpRxObservable.getObservable(user, getActivity(), FragmentEvent.PAUSE).subscribe(httpRxObserver);

    }


    /**
     * 注册
     *
     * @param mobile   手机号
     * @param captcha  验证码
     * @param password 密码
     */
    public void register(String mobile, String captcha, String password) {
        if (!PhoneUtils.isPhoneNumber(mobile)) {
            getView().error("请输入正确的手机号");
            return;
        }

        if (captcha.length() != 6) {
            getView().error("验证码位数不正确");
            return;
        }

        if (password.length() < 6) {
            getView().error("账号密码不能低于6位");
            return;
        }


        HttpRxObserver httpRxObserver = new HttpRxObserver<User>("register") {
            @Override
            protected void onStart(Disposable d) {
                getView().start();
            }

            @Override
            protected void onError(ApiException e) {
                getView().error(e.getMsg());
            }

            @Override
            protected void onSuccess(User user) {
                if (user.getJsonData() != null) {
                    SpUtils.putUser(user);
                    getView().success();
                } else {
                    getView().error(user.getError_msg());
                }
            }
        };


        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("captcha", captcha);
        map.put("password", AppSign.getPassword(password));
        map = AppSign.buildMap(map);

        Observable<User> user = NetUtils.getGsonRetrofit().
                register(map);
        HttpRxObservable.getObservable(user, getActivity(), FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }


    /**
     * 忘记密码
     *
     * @param mobile   手机号
     * @param captcha  验证码
     * @param password 密码
     */
    public void forget(String mobile, String captcha, String password) {
        if (!PhoneUtils.isPhoneNumber(mobile)) {
            getView().error("请输入正确的手机号");
            return;
        }

        if (captcha.length() != 6) {
            getView().error("验证码位数不正确");
            return;
        }

        if (password.length() < 6) {
            getView().error("账号密码不能低于6位");
            return;
        }


        HttpRxObserver httpRxObserver = new HttpRxObserver<User>("register") {
            @Override
            protected void onStart(Disposable d) {
                getView().start();
            }

            @Override
            protected void onError(ApiException e) {
                getView().error(e.getMsg());
            }

            @Override
            protected void onSuccess(User user) {
                if (user.getJsonData() != null) {
                    SpUtils.putUser(user);
                    getView().success();
                } else {
                    getView().error("注册失败");
                }
            }
        };


        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("captcha", captcha);
        map.put("password", AppSign.getPassword(password));
        map.put("rest_pwd", "1");
        map = AppSign.buildMap(map);

        Observable<User> user = NetUtils.getGsonRetrofit().
                update(map);
        HttpRxObservable.getObservable(user, getActivity(), FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }


    /**
     * 获取验证码
     * <p>
     * 获取用途,1.注册,2.绑定手机,3.找回密码
     *
     * @param mobile 手机号
     */
    public void captcha(String mobile, String useType) {
        if (!PhoneUtils.isPhoneNumber(mobile)) {
            getView().error("请输入正确的手机号");
            return;
        }
        HttpRxObserver httpRxObserver = new HttpRxObserver<Captcha>("Captcha") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
                getView().error("验证码获取失败");
            }

            @Override
            protected void onSuccess(Captcha response) {
            }
        };
        // 1、注册的验证码    4、是验证码登录的
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("use_type", useType);
        map = AppSign.buildMap(map);
        Observable<Captcha> user = NetUtils.getGsonRetrofit().
                captcha(map);
        HttpRxObservable.getObservable(user, getActivity(), FragmentEvent.PAUSE).subscribe(httpRxObserver);


    }


    /**
     * 微信登录
     *
     * @param info
     */
    public void loginWexin(WexinInfo info) {

        HashMap<String, String> map = new HashMap<>();
        map.put("nick_name", info.getNickname());
        map.put("avatar", info.getHeadimgurl());
        map.put("openid", info.getOpenid());
        map.put("sex", info.getSex() + "");
        map.put("country", info.getCountry());
        map.put("province", info.getProvince());
        map.put("city", info.getCity());
        map = AppSign.buildMap(map);

        HttpRxObserver httpRxObserver = new HttpRxObserver<User>("loginWx") {
            @Override
            protected void onStart(Disposable d) {
                getView().start();
            }

            @Override
            protected void onError(ApiException e) {
                getView().error(e.getMsg());
            }

            @Override
            protected void onSuccess(User user) {
                SpUtils.putUser(user);
                getView().success();
            }
        };
        Observable<User> user = NetUtils.getGsonRetrofit().loginWx(map);
        HttpRxObservable.getObservable(user, getActivity(), FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }


    /**
     * 支付宝登录
     *
     * @param authResult
     */
    public void loginAli(final AuthResult authResult) {

        HashMap<String, String> map = new HashMap<>();
        map.put("auth_code", authResult.getAuthCode());
        map.put("openid", authResult.getAlipayOpenId());
        map = AppSign.buildMap(map);
        HttpRxObserver httpRxObserver = new HttpRxObserver<User>("loginAli") {
            @Override
            protected void onStart(Disposable d) {
                getView().start();
            }

            @Override
            protected void onError(ApiException e) {
                if (authResult.getMemo().contains("操作已经取消")){
                    Toast.makeText(getActivity().mActivity, "取消登录", Toast.LENGTH_LONG).show();
                }else {
                    getView().error(e.getMsg());
                }

            }


            @Override
            protected void onSuccess(User user) {
                SpUtils.putUser(user);
                getView().success();
            }
        };

        Observable<User> user = NetUtils.getGsonRetrofit().loginAli(map);
        HttpRxObservable.getObservable(user, getActivity(), FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }


}
