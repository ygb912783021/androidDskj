package dingshi.com.hibook.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.LifeCycleListener;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.utils.AppManager;
import dingshi.com.hibook.utils.SpUtils;
import dingshi.com.hibook.view.FuckDialog;
import dingshi.com.hibook.view.TitleView;

/**
 * @author wangqi
 *         http://www.jianshu.com/p/2a884e211a62
 */

public abstract class BaseActivity extends BaseUmengActivity {

    Unbinder unbinder;
    Toast toast;
    public User user;

    public TitleView titleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mListener != null) {
            mListener.onCreate(savedInstanceState);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        setContentView(R.layout.activity_base);
        View view = LayoutInflater.from(this).inflate(getLayoutId(), null, false);
        titleView = findViewById(R.id.base_titleview);
        FrameLayout controller = findViewById(R.id.base_controller);
        controller.addView(view);
        unbinder = ButterKnife.bind(this, view);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        user = SpUtils.getUser();
        initView(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        initTitle();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 获取布局id
     *
     * @return
     */
    public abstract int getLayoutId();


    public void showToast(String str) {
        toast.setText(str);
        toast.show();
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
        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_progress, null, false);
        progressDialog = new FuckDialog(this).addView(view, 0.3f).setCancelable(flag).builder();
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
     * 初始化标题栏
     */
    protected void initTitle() {
        titleView.setOnFinishListener(new TitleView.OnFinishListener() {
            @Override
            public void over() {
                onLeftClick();
                finish();
            }
        });
        titleView.setOnRightListener(new TitleView.OnRightClickListner() {
            @Override
            public void click() {
                onRightClick();
            }
        });
    }

    /**
     * 标题栏右边的点击事件
     */
    public void onRightClick() {

    }

    /**
     * 标题栏左边的点击事件
     */
    public void onLeftClick() {

    }


    /**
     * 隐藏标题栏
     */
    public void hideTitleBar() {
        titleView.setVisibility(View.GONE);
    }


    /**
     * 批量设置标题
     *
     * @param flag  是否显示返回按钮
     * @param title 居中标题
     */
    public void requestActionBarStyle(boolean flag, String title) {
        titleView.requestActionBarStyle(flag, title, "");
    }

    /**
     * 批量设置标题
     *
     * @param flag       是否显示返回按钮
     * @param title      居中标题
     * @param rightTitle 居右标题
     */
    public void requestActionBarStyle(boolean flag, String title, String rightTitle) {
        titleView.requestActionBarStyle(flag, title, rightTitle);
    }

    /**
     * 批量设置标题
     *
     * @param flag        是否显示返回按钮
     * @param title       居中标题
     * @param rightTitle  居右标题
     * @param imgResourse 居右图片
     */
    public void requestActionBarStyle(boolean flag, String title, String rightTitle, int imgResourse) {
        titleView.requestActionBarStyle(flag, title, rightTitle, imgResourse);
    }

    public InputMethodManager inputMethodManager;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mListener != null) {
            mListener.onRestart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mListener != null) {
            mListener.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.onDestroy();
        }
        unbinder.unbind();
        AppManager.getInstance().removeActivity(this);
    }

    /**
     * 回调生命周期，保持baseActivity与basePresent的生命周期一致
     */
    public LifeCycleListener mListener;

    public void setOnLifeCycleListener(LifeCycleListener listener) {
        mListener = listener;
    }
}
