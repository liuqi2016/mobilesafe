package event.study.liuqi.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;

/**
 * Created by liuqi on 2016/11/2.
 */
public class LocationService extends Service {

    private String tag ="LocationService";

    @Override
    public void onCreate() {
        Log.i(tag,"进入定位");
        super.onCreate();
        //获取手机的经纬度坐标
        //1.获取位置管理者
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //2.以最优的方式来获取
        Criteria criteria = new Criteria();
        //允许流量
        criteria.setCostAllowed(true);
        //精确度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);

        //3.在一定时间间隔或者一定位置获取位置
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.i(tag,"权限成功");
        lm.requestLocationUpdates(bestProvider, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //经度
                double longitude = location.getLongitude();
                // 纬度
                double latitude = location.getLatitude();
                Log.i(tag,"经度:"+longitude+",纬度:"+latitude);
                //发送短信
                SmsManager smsManager = SmsManager.getDefault();
                String safenumber = SpUtils.getString(getApplicationContext(), ConstansValue.SAFENUMBER, "");
                smsManager.sendTextMessage(+86+"safenumber",null,"经度:"+longitude+"，纬度:"+latitude,null,null);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i(tag,"onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i(tag,"onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i(tag,"onProviderDisabled");
            }
        });
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
