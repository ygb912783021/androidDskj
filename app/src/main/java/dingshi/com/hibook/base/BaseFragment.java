package dingshi.com.hibook.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dingshi.com.hibook.R;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.utils.SpUtils;
import dingshi.com.hibook.view.FuckDialog;

/**
 * @author wangqi
 *         Created by apple on 2017/10/25.
 */


public abstract class BaseFragment extends com.trello.rxlifecycle2.components.support.RxFragment {
    public Activity mActivity;
    public View mView;
    public Toast toast;
    boolean isLoad = false;
    boolean isInit = false;

    private Unbinder unbinder;

    public User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        mView = inflater.inflate(getLayoutId(), container, false);
        toast = Toast.makeText(mActivity, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        unbinder = ButterKnife.bind(this, mView);
        user = SpUtils.getUser();
        initView();
        isInit = true;
        isCanLoadData();
        return mView;
    }

    /**
     * layout id
     *
     * @return 布局id
     */
    public abstract int getLayoutId();

    /**
     * 初始化view
     */
    public abstract void initView();

    public void showToast(String str) {
        toast.setText(str);
        toast.show();
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 懒加载
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }
        if (getUserVisibleHint() && !isLoad) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        unbinder.unbind();
    }

    /**
     * 第一次加载的处理   此处可以留给加载网络去处理
     */
    public void lazyLoad() {

    }

    /**
     * 页面停止加载的处理
     */
    public void stopLoad() {

    }

    /**
     * 显示正在加载对话框
     */
    public FuckDialog progressDialog;

    /**
     * @param tip  提示文字
     * @param flag 是否触摸取消
     */
    public void showProgressDialog(String tip, boolean flag) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_dialog_progress, null, false);
        progressDialog = new FuckDialog(getActivity()).addView(view, 0.3f).setCancelable(flag).builder();
        progressDialog.show();
        TextView textView = view.findViewById(R.id.dialog_progress_tip);
        textView.setText(tip);
    }

    /**
     * 关闭正在加载对话框
     */
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dissmis();
        }
    }


    /**
     * 增加fragment的title
     */
    String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


}
