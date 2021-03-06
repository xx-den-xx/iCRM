package ru.bda.icrm.holders;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 10.08.2016.
 */
public class AppPref {

    private static volatile AppPref instance;
    private SharedPreferences sPref;

    private static final String PREF_NAME = "app_pref";
    public static final String PREF_HEX_LOGIN = "hex_login";
    public static final String PREF_LOGIN = "login";
    public static final String PREF_HEX_PASSWORD = "hex_pass";
    public static final String PREF_TOKEN = "token";
    public static final String PREF_TIME_LOAD = "time_load";
    public static final String PREF_NOTIF_COUNT = "notif_count";
    public static final String PREF_ID = "id_manager";

    private AppPref() {}

    public static AppPref getInstance() {
        if (instance == null) {
            synchronized (AppPref.class) {
                if (instance == null) {
                    instance = new AppPref();
                }
            }
        }
        return instance;
    }

    public void setHexAuth(String login, String hexLogin, String hexPassword, String id, Context context) {
        sPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(PREF_LOGIN, login);
        ed.putString(PREF_HEX_LOGIN, hexLogin);
        ed.putString(PREF_HEX_PASSWORD, hexPassword);
        ed.putString(PREF_ID, id);
        ed.commit();
    }

    public void setToken(String token, Context context) {
        sPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(PREF_TOKEN, token);
        ed.commit();
    }

    public void setDateAuth(long time, Context context) {
        sPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(PREF_TIME_LOAD, time);
        ed.commit();
    }

    public long getDate(Context context) {
        sPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sPref.getLong(AppPref.PREF_TIME_LOAD, 0);
    }

    public void setNotifCount (int count, Context context) {
        sPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(PREF_NOTIF_COUNT, count);
        ed.commit();
    }

    public String getStringPref(String key, Context context) {
        sPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sPref.getString(key, "");
    }

    public int getNotifCount(Context context) {
        sPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sPref.getInt(PREF_NOTIF_COUNT, 0);
    }
}
