package ru.bda.icrm.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.model.LocationObject;
import ru.bda.icrm.model.Model;
import ru.bda.icrm.model.ModelImpl;
import ru.bda.icrm.model.dto.AnswerServerDTO;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class MyLocationManager implements LocationListener {

    private Context context;
    private LocationManager locationManager;
    private String token;
    private Model model = new ModelImpl();

    public MyLocationManager(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        token = AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, context);
    }

    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void startLocating() {
        Log.d("log_location", "start locating!!!!");
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("log_location", "stop with exit permission!!!!");
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Log.d("log_location", "start GPS_PROVIDER!!!!");
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            Log.d("log_location", "start NETWORK_PROVIDER!!!!");
        }

    }

    public void stopLocating () {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            locationManager.removeUpdates(this);
        Log.d("log_location", "stop locating!!!!");
    }

    private void showLocation(Location location) {
        if (location == null) {
            return;
        }
        LocationObject locationObject = new LocationObject();
        locationObject.setLat(location.getLatitude());
        locationObject.setLng(location.getLongitude());
        locationObject.setTime(location.getTime());
        locationObject.setToken(token);
        Subscription locSubscription = model.setGeoPoint(locationObject).subscribe(new Observer<AnswerServerDTO>() {
            @Override
            public void onCompleted() {
                Log.d("log_location", "Complete location");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("log_location", e.toString());
            }

            @Override
            public void onNext(AnswerServerDTO dto) {
                Log.d("log_location", "onNext code = " + dto.getState());
            }
        });
        Log.d("log_location", locationObject.toString());
        if (locationObject != null) {
            stopLocating();
        }
    }
}
