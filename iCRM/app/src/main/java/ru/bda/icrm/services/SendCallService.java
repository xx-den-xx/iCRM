package ru.bda.icrm.services;

import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.database.DBController;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.model.Call;

public class SendCallService extends IntentService {

    private DBController dbController;
    private List<Call> mCallList = new ArrayList<>();

    public SendCallService() {
        super("send call crm service");
        dbController = new DBController(SendCallService.this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (DBController.class) {
            mCallList = dbController.getCallList(true);
            List<Call> list = new ArrayList<>();
            if (mCallList != null) {
                for (int i = 0; i < mCallList.size(); i++) {
                    Call call = mCallList.get(i);
                    if (!call.isSend()) {
                        list.add(call);
                    }
                }
            }
            if (list != null && list.size() > 0) {
                for (Call call : list) {
                    dbController.updateCall(call);
                }
                ApiController.getInstance().addCall(
                        AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, SendCallService.this),
                        list
                );
            }
            dbController.closeDb();
        }
    }
}
