package dingshi.com.hibook.share;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author wangqi
 * @since 2017/11/18 18:23
 */

public class HttpUtils {
    public static String getHtml(String path) {
        StringBuilder result = new StringBuilder("");
        HttpURLConnection conn;
        BufferedReader br;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String len;
            while ((len = br.readLine()) != null) {
                result.append(len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
