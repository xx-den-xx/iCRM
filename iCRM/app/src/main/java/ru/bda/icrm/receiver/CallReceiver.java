package ru.bda.icrm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.services.CallService;

/**
 * Created by User on 17.11.2016.
 */

public class CallReceiver extends BroadcastReceiver {

    String phoneNumber = "";
    String type = "";

    public void onReceive(Context context, Intent intent) {
        Intent callIntent = new Intent(context, CallService.class);
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            //получаем исходящий номер
            phoneNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            type = "Исходящий";
            callIntent.putExtra(Constants.INTENT_CALL_PHONE, phoneNumber);
            callIntent.putExtra(Constants.INTENT_CALL_TYPE, type);
            context.startService(callIntent);
        } else if (intent.getAction().equals("android.intent.action.PHONE_STATE")){
            String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                //телефон звонит, получаем входящий номер
                phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                type = "Входящий";
                callIntent.putExtra(Constants.INTENT_CALL_PHONE, phoneNumber);
                callIntent.putExtra(Constants.INTENT_CALL_TYPE, type);
                context.startService(callIntent);

            } else if (phone_state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                //телефон находиться в ждущем режиме. Это событие наступает по окончанию разговора,
                // когда мы уже знаем номер и факт звонка

            }
        }
    }
}
