package ru.bda.icrm.map;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ru.bda.icrm.dialog.MyDialog;
import ru.bda.icrm.listener.OnMapClickListener;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.map.GeoCode;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.map.GeoCodeListener;
import ru.yandex.yandexmapkit.utils.ScreenPoint;

public class OverlayGeoCode extends Overlay implements GeoCodeListener {

    private OnMapClickListener mapListener;
    private AppCompatActivity ctx;


    public OverlayGeoCode(MapController mapController, AppCompatActivity ctx) {
        super(mapController);
        this.ctx = ctx;
    }

    @Override
    public boolean onSingleTapUp(float x, float y) {
        getMapController().getDownloader().getGeoCode(this, getMapController().getGeoPoint(new ScreenPoint(x, y)));
        return true;
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }

    public void setOnMapClickListener(OnMapClickListener mapListener) {
        this.mapListener = mapListener;
    }

    @Override
    public boolean onFinishGeoCode(final GeoCode geoCode) {
        if (geoCode != null) {
            Log.d("myLog", "lat = " + geoCode.getGeoPoint().getLat() + "; lng = " + geoCode.getGeoPoint().getLon());
            getMapController().getMapView().post(() -> {
                MyDialog dialog = new MyDialog();
                dialog.init("Сохранить координаты", geoCode.getDisplayName(), "Отмена", "Сохранить", new MyDialog.OnClickListener() {
                    @Override
                    public void onLeftBtnClick() {

                    }

                    @Override
                    public void onRightBtnClick() {
                        mapListener.onMapClick(geoCode.getGeoPoint().getLat(), geoCode.getGeoPoint().getLon());
                    }
                });
                dialog.show(ctx);
            });
        }
        return true;
    }
}
