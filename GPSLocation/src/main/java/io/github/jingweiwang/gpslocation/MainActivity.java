package io.github.jingweiwang.gpslocation;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView show;
    private LocationManager locationManager;
    private String provider;
    private int updateCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView providers = (ListView) findViewById(R.id.providers);
        show = (TextView) findViewById(R.id.show);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkGPSSetting();
        List<String> providerNames = locationManager.getAllProviders();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, providerNames);
        providers.setAdapter(adapter);
        getLocation();
    }

    private void checkGPSSetting() {
        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "请开启GPS! ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent, 0); //此为设置完成后返回到获取界面
    }

    private void getLocation() {
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);//免费
        criteria.setAltitudeRequired(true);//提供高度
        criteria.setBearingRequired(true);//提供方向
        criteria.setSpeedRequired(true);//提供速度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//精度
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);//方位信息精度
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);//速度精度
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);//水平精度
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);//垂直精度
        criteria.setPowerRequirement(Criteria.POWER_HIGH);//对电量的要求
        provider = locationManager.getBestProvider(criteria, true); //找到最好且能用的Provider
        Log.i("provider", provider);
        Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
        updateToNewLocation(location);

        //设置监听器, 自动更新的最小时间为间隔1000秒或最小位移变化超过10米
        locationManager.requestLocationUpdates(provider, 1000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateToNewLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                updateToNewLocation(locationManager.getLastKnownLocation(provider));
            }

            @Override
            public void onProviderDisabled(String provider) {
                updateToNewLocation(null);
            }
        });

    }

    private void updateToNewLocation(Location location) {
        if (location != null) {
            show.setText("provider" + provider + "\n经度:" + location.getLongitude() + "\n纬度:" + location.getLatitude() + "\n高度:" + location.getAltitude() + "\n更新次数:" + updateCount++);
            Log.i("经度", "" + location.getLongitude());
            Log.i("纬度", "" + location.getLatitude());
            Log.i("高度", "" + location.getAltitude());
        }
    }


}
