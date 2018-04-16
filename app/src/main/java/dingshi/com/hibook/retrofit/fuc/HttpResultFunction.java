package dingshi.com.hibook.retrofit.fuc;


import android.support.annotation.NonNull;

import com.yuyh.library.imgsel.utils.LogUtils;

import dingshi.com.hibook.retrofit.exception.ExceptionEngine;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * http结果处理函数
 */
public class HttpResultFunction<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
        //打印具体错误
        LogUtils.e("HttpResultFunction:" + throwable);
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
