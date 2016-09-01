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
    public static final String PREF_HEX_PASSWORD = "hex_pass";
    public static final String PREF_TOKEN = "token";

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

    public void setHexAuth(String hexLogin, String hexPassword, Context context) {
        sPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(PREF_HEX_LOGIN, hexLogin);
        ed.putString(PREF_HEX_PASSWORD, hexPassword);
        ed.commit();
    }

    public void setToken(String token, Context context) {
        sPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(PREF_TOKEN, token);
        ed.commit();
    }

    public String getStringPref(String key, Context context) {
        sPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sPref.getString(key, "");
    }
}
