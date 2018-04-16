package dingshi.com.hibook.retrofit.net;

import android.util.Log;

import java.io.IOException;

import dingshi.com.hibook.utils.SpUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author wangqi
 * @since 2017/12/14 09:53
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .header("Authorization", SpUtils.getUser().getToken())
                .build();
        return chain.proceed(request);
    }
}
