package ru.bda.icrm.map;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import ru.bda.icrm.listener.OnMapClickListener;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.map.GeoCode;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.map.GeoCodeListener;
import ru.yandex.yandexmapkit.utils.ScreenPoint;

/**
 * Created by User on 28.10.2016.
 */

public class OverlayGeoCode extends Overlay implements GeoCodeListener {

    private OnMapClickListener mapListener;


    public OverlayGeoCode(MapController mapController) {
        super(mapController);
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
            getMapController().getMapView().post(new Runnable() {
                @Override
                public void run() {
                        final AlertDialog.Builder dialog = new AlertDialog.Builder( getMapController().getContext());
                    dialog.setTitle(geoCode.getDisplayName());
                    dialog.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mapListener.onMapClick(geoCode.getGeoPoint().getLat(), geoCode.getGeoPoint().getLon());
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });
        }
        return true;
    }
}
