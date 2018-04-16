package dingshi.com.hibook.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.igexin.sdk.PushManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.db.City;
import dingshi.com.hibook.db.CityDao;
import dingshi.com.hibook.db.SQLTools;

public class CityActivity extends BaseActivity {

    public static final int CITY_REQUEST_CODE = 0x999;
    private static final int REQUEST_PERMISSION = 0;


    @BindView(R.id.city_province_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.city_province_recycle)
    RecyclerView provinRecycle;
    @BindView(R.id.city_region_recycle)
    RecyclerView regionRecycle;

    SQLiteDatabase sqLiteDatabase;

    FuckYouAdapter provinAdapter;
    FuckYouAdapter regionAdapter;

    List<City> provinList = new ArrayList<>();
    List<City> regionList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_city;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "城市选择");

        provinAdapter = new FuckYouAdapter<>(R.layout.view_item_city, provinList);
        regionAdapter = new FuckYouAdapter<>(R.layout.view_item_city, regionList);

        provinRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        regionRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        provinRecycle.setAdapter(provinAdapter);
        regionRecycle.setAdapter(regionAdapter);


        provinAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                City city = (City) item;
                helper.setText(R.id.view_item_city, city.getName());
            }
        });
        regionAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                City city = (City) item;
                helper.setText(R.id.view_item_city, city.getName());
                helper.getView(R.id.view_item_jump).setVisibility(View.GONE);
            }
        });

        provinAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                regionList.clear();
                regionList.addAll(CityDao.queryRegion(sqLiteDatabase, provinList.get(position).getId()));
                //如果省没有区的话，直接返回省
                if (regionList.size() == 0) {
                    backData(provinList.get(position));
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                    regionAdapter.notifyDataSetChanged();
                }

            }
        });

        regionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                drawerLayout.closeDrawers();
                //返回数据
                backData(regionList.get(position));
            }
        });
        /*
         *    禁止侧滑
         */
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        AndPermission.with(this)
                .requestCode(200)
                .permission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        if (requestCode == 200) {
                            sqLiteDatabase = SQLTools.opendatabase(CityActivity.this);
                            provinList.addAll(CityDao.queryProvince(sqLiteDatabase));
                            provinAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        showToast("请从应用信息里面打开权限");
                    }
                }).start();
    }


    public void backData(City city) {
        Intent intent = getIntent();
        intent.putExtra("city", city);
        setResult(CITY_REQUEST_CODE, intent);
        finish();
    }

//    public void query(View v) {
//    }
//
//    public void region(View view) {
//        CityDao.queryRegion(sqLiteDatabase, "1");
//    }
//
//    public void query_city(View view) {
//        CityDao.queryCity(sqLiteDatabase, "610000");
//    }
}
