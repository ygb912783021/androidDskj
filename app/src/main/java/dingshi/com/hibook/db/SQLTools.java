package dingshi.com.hibook.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author wangqi
 * @since 2017/12/20 14:18
 */

public class SQLTools {

    public static SQLiteDatabase opendatabase(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/regions.db");
            if (!file.exists()) {
                AssetManager assetManager = context.getAssets();
                try {
                    // 获取输入流
                    InputStream is = assetManager.open("regions.db");
                    FileOutputStream fos = new FileOutputStream(file);
                    // 开始读和写
                    byte[] bys = new byte[1024];
                    int len;
                    while ((len = is.read(bys)) != -1) {
                        fos.write(bys, 0, len);
                    }
                    is.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return SQLiteDatabase.openOrCreateDatabase(file, null);
        }

        return null;
    }
}