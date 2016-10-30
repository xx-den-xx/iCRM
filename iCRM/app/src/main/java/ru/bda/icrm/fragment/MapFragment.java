package ru.bda.icrm.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.bda.icrm.R;
import ru.bda.icrm.listener.OnMapClickListener;
import ru.bda.icrm.map.OverlayGeoCode;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.map.MapEvent;
import ru.yandex.yandexmapkit.map.OnMapListener;
import ru.yandex.yandexmapkit.overlay.location.MyLocationOverlay;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by User on 31.08.2016.
 */
public class MapFragment extends Fragment {

    private MapView mMapView;
    private OverlayGeoCode overlay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        initContent(view);

        mMapView.showBuiltInScreenButtons(true);

        MapController mMapController = mMapView.getMapController();

        mMapController.setPositionAnimationTo(new GeoPoint());
        mMapController.setZoomCurrent(15);

        overlay = new OverlayGeoCode(mMapController);
        overlay.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(double lat, double lon) {

            }
        });
        mMapController.getOverlayManager().addOverlay(overlay);
        return view;
    }

    private void initContent(View view) {
        mMapView = (MapView) view.findViewById(R.id.map);
    }
}
