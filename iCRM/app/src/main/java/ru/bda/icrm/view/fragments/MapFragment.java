package ru.bda.icrm.view.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.Token;
import ru.bda.icrm.presenter.MapFragmentPresenter;
import ru.bda.icrm.view.MapFragmentView;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class MapFragment extends Fragment implements MapFragmentView{

    private MapView mMapView;
    private Overlay overlay;
    private List<Contragent> mListContragent = new ArrayList<>();
    private MapController mMapController;
    private MapFragmentPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        initContent(view);

        presenter = new MapFragmentPresenter(this);
        mMapView.showBuiltInScreenButtons(true);
        presenter.loadData(new Token(AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getContext())));
        //new ContragentRequestTask().execute();
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

    @Override
    public void showError(String error) {

    }

    @Override
    public void loadContragents(List<Contragent> contragents) {
        if (contragents != null) {
            mListContragent.addAll(contragents);
        }
        showObject(mListContragent);
    }
}
