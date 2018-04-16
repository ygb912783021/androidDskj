package dingshi.com.hibook.base;

import android.os.Bundle;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import dingshi.com.hibook.action.LifeCycleListener;

/**
 * @author wangqi
 *         Created by Administrator on 2017/9/20.
 */

public class BasePresent<V, A> implements LifeCycleListener {

    private V mView;
    private Reference<V> mVrefrence;
    private A mActivity;
    private Reference<A> mArefrence;


    public BasePresent(V view, A activity) {
        attachView(view);
        attachActivity(activity);
        setListener(activity);
    }

    /**
     * 关联Activity
     *
     * @param activity
     */
    private void attachActivity(A activity) {
        mArefrence = new WeakReference<A>(activity);
        mActivity = mArefrence.get();
    }

    /**
     * 关联Activity的实现类
     *
     * @param view
     */
    private void attachView(V view) {
        mVrefrence = new WeakReference<V>(view);
        mView = mVrefrence.get();
    }


    /**
     * 设置生命周期监听
     *
     * @author ZhongDaFeng
     */
    private void setListener(A activity) {
        if (getActivity() != null) {
            if (activity instanceof BaseActivity) {
                ((BaseActivity) getActivity()).setOnLifeCycleListener(this);
            }
        }
    }

    /**
     * 获取Activity
     *
     * @return
     */
    public A getActivity() {
        if (mArefrence != null) {
            return mArefrence.get();
        } else {
            return null;
        }
    }

    /**
     * 获取Activity的实现类
     *
     * @return
     */
    public V getView() {
        if (mVrefrence.get() != null) {
            return mVrefrence.get();
        } else {
            return null;
        }
    }

    /**
     * 判断当前是否已关联软引用
     */

    /**
     * 是否已经关联
     *
     * @return
     */
    public boolean isViewAttached() {
        return mVrefrence != null && mVrefrence.get() != null;
    }

    /**
     * 是否已经关联
     *
     * @return
     */
    public boolean isActivityAttached() {
        return mArefrence != null && mArefrence.get() != null;
    }


    /**
     * 删除Activity弱引用
     */
    private void detachActivity() {
        if (mArefrence.get() != null) {
            mArefrence.clear();
            mArefrence = null;
        }
    }

    /**
     * 删除Activity实现类的 弱引用
     */
    private void detachView() {
        if (mVrefrence.get() != null) {
            mVrefrence.clear();
            mVrefrence = null;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        detachView();
        detachActivity();
    }

}
