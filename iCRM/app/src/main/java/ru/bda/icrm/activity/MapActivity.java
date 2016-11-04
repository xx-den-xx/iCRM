package ru.bda.icrm.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import ru.bda.icrm.R;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.OnMapClickListener;
import ru.bda.icrm.map.OverlayGeoCode;
import ru.bda.icrm.model.Contragent;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.overlay.location.MyLocationItem;
import ru.yandex.yandexmapkit.overlay.location.OnMyLocationListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by User on 30.10.2016.
 */

public class MapActivity extends AppCompatActivity implements OnMyLocationListener{

    private MapView mMapView;
    private OverlayGeoCode overlay;
    private Contragent mContragent;
    private Toolbar mToolbar;
    private Context mContext;
    private ProgressBar mProgressBar;
    private String id;
    private String token;
    private MapController mMapController;
    private OverlayItem item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mContext = this;
        initContent();
        setToolbar();
        token = AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, mContext);
        if (getIntent() != null) {
            id = getIntent().getStringExtra(Constants.INTENT_ID_CONTRAGENT);
        }

        new ContragentTask().execute();
    }

    private void setMapPosition(double lat, double lon) {

        GeoPoint point = new GeoPoint();

        if (mContragent != null ) {
            if (lat != 0  && lon != 0 ) {
                point = new GeoPoint( lat, lon);
            }
        }
        mMapController.setPositionAnimationTo(point);
        mMapController.setZoomCurrent(20);

        overlay = new OverlayGeoCode(mMapController, this);
        overlay.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(double lat, double lon) {
                mContragent.setLat(lat);
                mContragent.setLon(lon);
                new UpdateContragentTask().execute();
            }
        });
        mMapController.getOverlayManager().addOverlay(overlay);
    }

    private void setMyPosition() {
        mMapController.getOverlayManager().getMyLocation().addMyLocationListener(this);
    }

    private void initContent() {
        mMapView = (MapView) findViewById(R.id.map);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mMapController = mMapView.getMapController();
        mMapView.showBuiltInScreenButtons(true);
        overlay = new OverlayGeoCode(mMapController, this);
        overlay.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(double lat, double lon) {
                mContragent.setLat(lat);
                mContragent.setLon(lon);
                new UpdateContragentTask().execute();
            }
        });
        mMapController.getOverlayManager().addOverlay(overlay);
        setMyPosition();
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showObject(double lat, double lon) {
        overlay.clearOverlayItems();

        Resources res = getResources();
        item = new OverlayItem(new GeoPoint(lat, lon),
                res.getDrawable(R.drawable.ymk_user_location_lbs));
        BalloonItem balloon = new BalloonItem(this, item.getGeoPoint());
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
    }

    @Override
    public void onMyLocationChange(final MyLocationItem myLocationItem) {
        final double lat = myLocationItem.getGeoPoint().getLat();
        final double lon = myLocationItem.getGeoPoint().getLon();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Log.d("myLog", myLocationItem.getGeoPoint().toString());
                setMapPosition(lat, lon);
                showObject(lat, lon);
            }
        });
    }

    private class ContragentTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mContragent = ApiController.getInstance().getContragent(token, id);
            boolean answer = mContragent != null ? true : false;
            return answer;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Log.d("myLog", mContragent.toString());
                mToolbar.setTitle(mContragent.getNameContragent());
                mProgressBar.setVisibility(View.GONE);
                if (mContragent.getLat() > 0 && mContragent.getLon() > 0) {
                    setMapPosition(mContragent.getLat(), mContragent.getLon());
                    showObject(mContragent.getLat(), mContragent.getLon());
                } else {
                    setMyPosition();
                }
            } else {
                Log.d("myLog", "ошибка получения контрагента");
            }
        }
    }

    private class UpdateContragentTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ApiController.getInstance().addContragent(token, mContragent.getId(), mContragent);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            showObject(mContragent.getLat(), mContragent.getLon());
        }
    }
}
