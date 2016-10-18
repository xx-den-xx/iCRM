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

    public String getDateString(int year, int monthOfYear, int dayOfMonth) {
        String date = (String.valueOf(dayOfMonth).length() < 2 ? "0" + dayOfMonth : "" + dayOfMonth) +
                "/" + (String.valueOf(monthOfYear).length() < 2 ? "0" + monthOfYear : "" + monthOfYear) +
                "/" + (String.valueOf(year).length() < 2 ? "0" + year : "" + year);
        return date;
    }
}
