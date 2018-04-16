package dingshi.com.hibook.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//import com.android.internal.util.XmlUtils;

/**
 * @author wangqi
 * @since 2018/1/18 11:15
 */

public class CityDao {


    /**
     * 查询所有的省份
     *
     * @param sqLiteDatabase
     */
    public static List<City> queryProvince(SQLiteDatabase sqLiteDatabase) {
        List<City> list = new ArrayList<>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery("select id,name,id_card_prefix from region where region_id=0", null);
            int id = cursor.getColumnIndex("id");
            int name = cursor.getColumnIndex("name");
            int code = cursor.getColumnIndex("id_card_prefix");
            while (cursor.moveToNext()) {
                City city = new City();
                city.setId(cursor.getString(id));
                city.setName(cursor.getString(name));
                city.setCode(cursor.getString(code));
                list.add(city);
            }
            cursor.close();
        } catch (Exception e) {

        }

        return list;
    }

    /**
     * 根据id查询所有的区域
     *
     * @param sqLiteDatabase
     * @param ids
     * @return
     */
    public static List<City> queryRegion(SQLiteDatabase sqLiteDatabase, String ids) {
        List<City> list = new ArrayList<>();

        String sql;
        //上海市 重庆市  北京市  天津市 sql
        if ("1".equals(ids) || "19".equals(ids) || "2455".equals(ids) || "858".equals(ids)) {
            sql = "select * from region where region_id = (select id from region where region_id=" + ids + ")";
        } else {
            sql = "select * from region where region_id=" + ids;
        }
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            int id = cursor.getColumnIndex("id");
            int name = cursor.getColumnIndex("name");
            int code = cursor.getColumnIndex("id_card_prefix");
            while (cursor.moveToNext()) {
                City city = new City();
                city.setId(cursor.getString(id));
                city.setName(cursor.getString(name));
                city.setCode(cursor.getString(code));
                list.add(city);

            }
            cursor.close();
        } catch (Exception e) {

        }

        return list;
    }

    /**
     * 根据id查询城市
     *
     * @param sqLiteDatabase
     * @param ids
     * @return
     */
    public static City queryCity(SQLiteDatabase sqLiteDatabase, String ids) {
        City city = new City();
        try {
            city.setCode(ids);
            Cursor cursor = sqLiteDatabase.rawQuery("select name from region where id_card_prefix=" + ids, null);
            int name = cursor.getColumnIndex("name");
            while (cursor.moveToNext()) {

                city.setName(cursor.getString(name));
            }
            cursor.close();
        } catch (Exception e) {

        }
        return city;
    }


    public static void query(SQLiteDatabase sqLiteDatabase) {


        try {
            Cursor cursor = sqLiteDatabase.rawQuery("select * from region ", null);
            while (cursor.moveToNext()) {
                String name = "";
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    int index = cursor.getColumnIndex(cursor.getColumnName(i));
                    name += cursor.getString(index) + "\t";
                }
                Log.i("name", name);
            }
            cursor.close();
        } catch (Exception e) {

        }
    }
}
