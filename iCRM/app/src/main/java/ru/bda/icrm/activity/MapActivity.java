package ru.bda.icrm.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by User on 30.10.2016.
 */

public class MapActivity extends AppCompatActivity {

    private MapView mMapView;
    private OverlayGeoCode overlay;
    private Contragent mContragent;
    private Toolbar mToolbar;
    private Context mContext;
    private ProgressBar mProgressBar;
    private String id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (getIntent() != null) {
            id = getIntent().getStringExtra(Constants.INTENT_UID_CONTRAGENT);
            new ContragentTask().execute();
        }

        initContent();
        setToolbar();
        mMapView.showBuiltInScreenButtons(true);

        MapController mMapController = mMapView.getMapController();

        mMapController.setPositionAnimationTo(new GeoPoint());
        mMapController.setZoomCurrent(15);

        overlay = new OverlayGeoCode(mMapController);
        overlay.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(double lat, double lon) {
                mContragent.setLat(lat);
                mContragent.setLon(lon);
                showObject();
            }
        });
        mMapController.getOverlayManager().addOverlay(overlay);
    }

    private void initContent() {
        mMapView = (MapView) findViewById(R.id.map);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
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

    private void showObject() {
        Resources res = getResources();
        OverlayItem item = new OverlayItem(new GeoPoint(mContragent.getLon(), mContragent.getLat()),
                getResources().getDrawable(R.drawable.ic_place_black_24dp));
    }

    private class ContragentTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mContragent = ApiController.getInstance().getContragent(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, mContext), id);
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
                if (mContragent.getLat() > 0 && mContragent.getLon() > 0);
                showObject();
            } else {
                Log.d("myLog", "ошибка получения контрагента");
            }
        }
    }
}
