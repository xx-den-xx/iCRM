package ru.bda.icrm.holders;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by User on 06.09.2016.
 */
public class AppControl {

    private static volatile AppControl instance;

    private AppControl() {

    }

    public static AppControl getInstance() {
        if (instance == null) {
            synchronized (AppControl.class) {
                if (instance == null) {
                    instance = new AppControl();
                }
            }
        }
        return instance;
    }

    public boolean isOnline (Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if(netInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
