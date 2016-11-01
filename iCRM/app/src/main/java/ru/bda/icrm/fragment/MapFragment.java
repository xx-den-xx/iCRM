package ru.bda.icrm.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.activity.MapActivity;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.OnMapClickListener;
import ru.bda.icrm.map.OverlayGeoCode;
import ru.bda.icrm.model.Contragent;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.map.MapEvent;
import ru.yandex.yandexmapkit.map.OnMapListener;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.overlay.location.MyLocationOverlay;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by User on 31.08.2016.
 */
public class MapFragment extends Fragment {

    private MapView mMapView;
    private Overlay overlay;
    private List<Contragent> mListContragent = new ArrayList<>();
    private MapController mMapController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        initContent(view);

        mMapView.showBuiltInScreenButtons(true);
        new ContragentRequestTask().execute();
        return view;
    }

    private void showObject(List<Contragent> list) {
        Resources res = getResources();
        Contragent contrPoint = new Contragent();
        for (int i = 0; i < list.size(); i++) {
            Contragent mContragent = list.get(i);
            if (mContragent.getLon() != 0 && mContragent.getLat() != 0) {
                OverlayItem item = new OverlayItem(new GeoPoint(mContragent.getLat(), mContragent.getLon()),
                        res.getDrawable(R.drawable.ymk_user_location_lbs));
                BalloonItem balloon = new BalloonItem(getActivity(), item.getGeoPoint());
                balloon.setText(mContragent.getNameContragent());
                balloon.setOnBalloonListener(new OnBalloonListener() {
                    @Override
                    public void onBalloonViewClick(BalloonItem balloonItem, View view) {

                    }

                    @Override
                    public void onBalloonShow(BalloonItem balloonItem) {

                    }

                    @Override
                    public void onBalloonHide(BalloonItem balloonItem) {

                    }

                    @Override
                    public void onBalloonAnimationStart(BalloonItem balloonItem) {

                    }

                    @Override
                    public void onBalloonAnimationEnd(BalloonItem balloonItem) {

                    }
                });
                item.setBalloonItem(balloon);
                overlay.addOverlayItem(item);
                contrPoint = mContragent;
            }
        }

        MapController mMapController = mMapView.getMapController();

        GeoPoint point = new GeoPoint();

        if (contrPoint != null ) {
            if (contrPoint.getLat() != 0  && contrPoint.getLon() != 0 ) {
                point = new GeoPoint( contrPoint.getLat(), contrPoint.getLon());
            }
        }
        mMapController.setPositionAnimationTo(point);
        mMapController.setZoomCurrent(17);
    }


    private void initContent(View view) {
        mMapView = (MapView) view.findViewById(R.id.map);

        mMapController = mMapView.getMapController();

/**        GeoPoint point = new GeoPoint();

        if (mContragent != null ) {
            if (mContragent.getLat() != 0  && mContragent.getLon() != 0 ) {
                point = new GeoPoint( mContragent.getLat(), mContragent.getLon());
            }
        }
        mMapController.setPositionAnimationTo(point);
        mMapController.setZoomCurrent(17);*/

        //overlay = new OverlayGeoCode(mMapController, (AppCompatActivity) getActivity());
        overlay = new Overlay(mMapController);
        mMapController.getOverlayManager().addOverlay(overlay);
    }

    private class ContragentRequestTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<Contragent> list = ApiController.getInstance()
                    .getContragentList(AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()));
            if (list != null) {
                mListContragent.addAll(list);
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            showObject(mListContragent);
        }
    }

}
