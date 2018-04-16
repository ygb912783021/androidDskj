package dingshi.com.hibook;

import android.content.SyncStatusObserver;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.solver.Goal;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.junit.Test;

import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dingshi.com.hibook.bean.Avatar;
import dingshi.com.hibook.bean.Captcha;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.retrofit.api.ApiService;
import dingshi.com.hibook.retrofit.gson.ResponseConverterFactory;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.EmailUtils;
import dingshi.com.hibook.utils.SpUtils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);


//        System.out.print(this.getClass());

//        Captcha c = new Captcha();
//        Captcha.JsonDataBean bean = new Captcha.JsonDataBean();
//        bean.setCaptcha(123456);
//        c.setJsonData(bean);
//        Object result = c;
//
//        String a = new Gson().toJson(result);
//        System.out.println(a);
//
//        System.out.println(c.getClass().getName());


//        String result = "http://testwap.linkbooker.com?sn=su120010";

//        result = "http://testm.linkbooker.com/qrcode?user_id=123123123&card_id=u6789&timestamp=&os_type=&os_version=&channel=";

//        result="http://m.linkbooker.com/qrcode?user_id=111&card_id=222&channel=000&device_token=debug&os_type=android&os_version=999&timestamp=1514429655205";
//        String split[] = result.split("\\?");
        String http = "http://m.linkbooker.com/users/signin?catalog=1&user_id=2&type=1&book_num=1&sbook_num=1";
//        if (split.length > 1) {
//            String values[] = split[1].split("&");
//            for (int i = 0; i < values.length; i++) {
//                if (values[i].contains("sn=")) {
//                    System.out.println(values[i].substring(values[i].indexOf("=") + 1, values[i].length()));
//                }
//                if (values[i].contains("user_id=")) {
//                    System.out.println(values[i].substring(values[i].indexOf("=") + 1, values[i].length()));
//                }
//                if (values[i].contains("card_id=")) {
//                    System.out.println(values[i].substring(values[i].indexOf("=") + 1, values[i].length()));
//                }
//
//            }
//        }

//        final ApiService apiService = new Retrofit.Builder()
//                .baseUrl("http://www.baidu.com")
//                .addConverterFactory(ResponseConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build().create(ApiService.class);
//
//        apiService.test().subscribe(new Consumer<ResponseBody>() {
//            @Override
//            public void accept(@NonNull ResponseBody responseBody) throws Exception {
//                System.out.print(responseBody.string());
//            }
//        });


//
//        HashMap<String, Avatar> map = new HashMap<>();
//        Avatar avatar = new Avatar("wangqi", "http");
//        map.put("user", avatar);
//
//        String json = new Gson().toJson(map);
//        System.out.println(json);
////
////
//        HashMap<String, Avatar> aa = new Gson().fromJson(json, HashMap.class);
//
////        Avatar avatar1 = new Gson().fromJson(aa.get("uid"), Avatar.class);
//        System.out.println(aa.get("uid").getAvatar());
//        byte[] encode = Base64.encode((password + APP_KEY).getBytes(), Base64.URL_SAFE);
//        String enc = new String(encode);

//        String enc = URLEncoder.encode("1234560OHgJsh+z,NKTD=8,VgA7AoLGS@rkwue");
//        System.out.print(enc);


//        String a=String.format("%s(%s)","1","2");
//        System.out.print(a);
//        if ("http://".startsWith("http://")){
//            System.out.print("123");
//        }

//        boolean flag = EmailUtils.isEmail("www@qq.wang");
//        System.out.print(flag);


        int arr[] = new int[8];
        getNext(arr, "abababcd");

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

    }

    //kmp求解next数组
    private void getNext(int[] next, String str) {
        next[0] = -1;//初始化
        int k = -1;//记录当前位的next
        int j = 0;//当前位下标
        while (j < str.length() - 1) {//求解完所有字符的next

            if (k == -1 || str.charAt(j) == str.charAt(k)) {//比较当前位与当前位next字符是否相等
                j++;
                k++;//当前位的next值+1作为下一位的next值
                next[j] = k;//求解出下一位的next值
            } else {
                k = next[k];//如果不相等则找当前位的next的next与当前位比较
            }
            //abababcd
            //当k=-1  j=0   >  j=1   k=0   next[1]=0
            //当k=0   j=1   >  j=2   k=0
            //当k=0   j=2   >  j=3   k=1   next[3]=1
            //当k=1   j=3   >  j=4   k=2   next[4]=2
            //当k=2   j=4   >  j=5   k=3   next[5]=3
            //当k=3   j=5   >  j=6   k=4   next[6]=4
            //当k=4   j=6   >  j=6   k=2
//            System.out.println(k+"---");
        }
    }

}