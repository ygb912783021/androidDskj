package dingshi.com.hibook.retrofit.listener;

import io.reactivex.disposables.Disposable;

/**
 * RxJavaAction管理接口
 *
 * @author ZhongDaFeng
 */

public interface RxActionManager<T> {
    /**
     * 添加
     *
     * @param tag
     * @param disposable
     */
    void add(T tag, Disposable disposable);

    /**
     * 移除
     *
     * @param tag
     */
    void remove(T tag);

    /**
     * 取消
     *
     * @param tag
     */
    void cancel(T tag);

}