package dingshi.com.hibook.retrofit.net;

import com.baidu.platform.comapi.map.E;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import dingshi.com.hibook.BuildConfig;
import dingshi.com.hibook.retrofit.api.ApiService;
import dingshi.com.hibook.retrofit.gson.ResponseConverterFactory;
import dingshi.com.hibook.retrofit.interceptroutils.HttpLogger;
import dingshi.com.hibook.utils.SpUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author wangqi
 * @since 2017/11/24 13:32
 */

public class NetUtils {

    public static String base_url = SpUtils.getBaseEnv();

    private static final Retrofit RETROFIT = new Retrofit.Builder()
//            .baseUrl(BuildConfig.LOCALHOST)
            .baseUrl(base_url)
            .client(getHttpClient())
            .addConverterFactory(ResponseConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();


    /**
     * RxJava gson
     *
     * @return
     */
    public static ApiService getGsonRetrofit() {
        return RETROFIT.create(ApiService.class);
    }

    public static ApiService getGsonCall() {
        return new Retrofit.Builder()
//                .baseUrl(BuildConfig.LOCALHOST)
                .baseUrl(base_url)

                .client(getHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build().create(ApiService.class);
    }

    /**
     * RxJava String
     *
     * @return
     */
    public static ApiService getStringRetrofit() {
        return new Retrofit.Builder()
//                .baseUrl(BuildConfig.LOCALHOST)
                .baseUrl(base_url)
                .client(getHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(ApiService.class);
    }

    private static OkHttpClient getHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLogger());
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor(new HttpLogger()))
                .addInterceptor(new HeaderInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .build();
    }



}
