package ru.bda.icrm.services;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import ru.bda.icrm.database.DBController;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.model.Call;
import ru.bda.icrm.model.Token;
import ru.bda.icrm.model.dto.AnswerServerDTO;
import ru.bda.icrm.presenter.MainActivityPresenter;
import ru.bda.icrm.view.MainActivityView;

/**
 * Created by User on 17.11.2016.
 */

public class CallService extends IntentService implements MainActivityView{

    private DBController dbController;
    private MainActivityPresenter presenter;

    public CallService() {
        super("call service");
        dbController = new DBController(CallService.this);
        presenter = new MainActivityPresenter(this, dbController);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String phone = intent.getStringExtra(Constants.INTENT_CALL_PHONE);
        String type = intent.getStringExtra(Constants.INTENT_CALL_TYPE);
        synchronized (DBController.class) {
            getCallDetails();
            dbController.closeDb();
        }
    }

    private void getCallDetails() {
        StringBuffer sb = new StringBuffer();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        long time = AppPref.getInstance().getDate(this);
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        sb.append( "Call Details :");
        while ( managedCursor.moveToNext() ) {
            Call call = new Call();
            String phNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            String callDuration = managedCursor.getString( duration );
            String dir = null;
            int dircode = Integer.parseInt( callType );
            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "Исходящий";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "Входящий";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "Пропущенный";
                    break;
            }
            call.setPhone(phNumber);
            call.setSend(false);
            call.setTime(Long.valueOf(callDate));
            call.setType(dir);
            call.setDuration(callDuration);
            if (time <= call.getTime()) dbController.addCall(call);
        }
        managedCursor.close();
        presenter.getCallLog(new Token(AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN,this)
                , AppPref.getInstance().getStringPref(AppPref.PREF_ID, this)));
    }

    @Override
    public void showError(String error) {
        //Toast.makeText(this, "Ошибка " + error, Toast.LENGTH_LONG).show();
        Log.e("log_call", error);
    }

    @Override
    public void sendCallLog(AnswerServerDTO answerServer) {
        String answer;
        if (answerServer.getState().equals("200")) {
            answer = "Звонки успешно отправлены на сервер!!!";
        } else {
            answer = "Звонки не отправлены на сервер!!!";
        }
        //Toast.makeText(this, answer, Toast.LENGTH_LONG).show();
        Log.d("log_call", answer);
    }
}
