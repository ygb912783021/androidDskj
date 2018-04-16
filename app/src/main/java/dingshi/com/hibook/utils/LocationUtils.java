package dingshi.com.hibook.utils;

import android.Manifest;
import android.app.Application;
import android.support.annotation.NonNull;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

/**
 * @author wangqi
 * @since 2018/1/15 17:00
 */

public class LocationUtils {

    private static LocationUtils locationUtils;

    public static LocationUtils getInstance() {
        if (locationUtils == null) {
            locationUtils = new LocationUtils();
        }
        return locationUtils;
    }

    LocationClient mLocClient;

    MyLocationListenner myListener = new MyLocationListenner();

    Application application;

    public void registerLocation(Application application, LocationListner locationListner) {
        this.locationListner = locationListner;
        this.application = application;

        SDKInitializer.initialize(application);
        mLocClient = new LocationClient(application);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//
        option.setCoorType("gcj02");
        option.setScanSpan(30000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
    }


    public void onPause() {
        if (mLocClient != null) {
            mLocClient.stop();
        }
    }

    public void onResume() {
        if (mLocClient != null) {
            startLocation();
        }
    }

    private void startLocation() {
        AndPermission.with(application)
                .requestCode(200)
                .permission(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        if (requestCode == 200) {
                            mLocClient.start();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                    }
                }).start();

    }


    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            locationListner.location(location.getLatitude(), location.getLongitude());
        }
    }

    public void unRegisterLocation() {
        mLocClient.unRegisterLocationListener(myListener);
        if (mLocClient != null) {
            mLocClient.stop();
        }
    }


    LocationListner locationListner;

    public void setLocationListner(LocationListner locationListner) {
        this.locationListner = locationListner;
    }

    public interface LocationListner {
        void location(double latitude, double longtitude);
    }
}
