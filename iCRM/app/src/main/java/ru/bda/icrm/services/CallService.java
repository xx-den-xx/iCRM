package ru.bda.icrm.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import ru.bda.icrm.database.DBController;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.model.Call;

/**
 * Created by User on 17.11.2016.
 */

public class CallService extends IntentService {

    private DBController dbController;

    public CallService() {
        super("call service");
        dbController = new DBController(CallService.this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String phone = intent.getStringExtra(Constants.INTENT_CALL_PHONE);
        int type = intent.getIntExtra(Constants.INTENT_CALL_TYPE, 0);
        Call call = new Call();
        call.setPhone(phone);
        call.setLogin(AppPref.getInstance().getStringPref(AppPref.PREF_LOGIN, getApplicationContext()));
        long time = Calendar.getInstance().getTimeInMillis();
        call.setTime(time);
        call.setType(type);
        call.setSend(false);
        Log.d("CallLog", call.toString());
        dbController.addCall(call);
    }
}
