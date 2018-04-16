package dingshi.com.hibook.retrofit.gson;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.exception.ServerException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author wangqi
 * @since 2017/11/28 19:44
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;


    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String response = value.string();
        Log.i("json",response);
        //先将返回的json数据解析到Response中，如果code==200，则解析到我们的实体基类中，否则抛异常
        Result httpResult = gson.fromJson(response, Result.class);


        if (httpResult.getError_code() == 0 || httpResult.getError_code() == -1) {
            //200的时候就直接解析，不可能出现解析异常。因为我们实体基类中传入的泛型，就是数据成功时候的格式
            return gson.fromJson(response, type);
            //无数据
        } else {
            throw new ServerException(httpResult.getError_code(), httpResult.getError_msg());
        }
    }
}
