package ru.bda.icrm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.model.Call;
import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.Event;

/**
 * Created by User on 28.06.2016.
 */
public class DBController {

    private static volatile DBController instance;
    private Context context;
    private DBHelper dbHelper;
    private ContentValues mContentValues;
    private SQLiteDatabase mDb;

    public DBController(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        mContentValues = new ContentValues();
    }

    public void insertContragentDB (List<Contragent> contragentList) {
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = mDb.query(DBHelper.TABLE_CONTRAGENT, null, null, null, null, null, null);
        boolean isHaveUID = false;
        for (Contragent contragent : contragentList) {
            if (cursor.moveToFirst()) {
                int idColIndex = cursor.getColumnIndex(DBHelper.UID);
                do {
                    if (!isHaveUID) isHaveUID = cursor.getString(idColIndex).equals(contragent.getUid());
                } while (cursor.moveToNext());
                if (!isHaveUID) {
                    mContentValues.put(DBHelper.UID, contragent.getUid());
                    mContentValues.put(DBHelper.NAME_CONTRAGENT, contragent.getNameContragent());
                    if (mDb != null) mDb.insert(DBHelper.TABLE_CONTRAGENT, null, mContentValues);
                }
                isHaveUID = false;
            } else {
                mContentValues.put(DBHelper.UID, contragent.getUid());
                mContentValues.put(DBHelper.NAME_CONTRAGENT, contragent.getNameContragent());
                if (mDb != null) mDb.insert(DBHelper.TABLE_CONTRAGENT, null, mContentValues);
            }
        }
        dbHelper.close();
    }

    public List<Contragent> getContragentList() {
        mDb = dbHelper.getWritableDatabase();
        List<Contragent> contragentList = new ArrayList<>();
        Cursor cursor = mDb.query(DBHelper.TABLE_CONTRAGENT, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex(DBHelper.ID);
            int uidColIndex = cursor.getColumnIndex(DBHelper.UID);
            int nameColIndex = cursor.getColumnIndex(DBHelper.NAME_CONTRAGENT);
            int innColIndex = cursor.getColumnIndex(DBHelper.INN);
            int codeColIndex = cursor.getColumnIndex(DBHelper.CODE);
            int jurFaceColIndex = cursor.getColumnIndex(DBHelper.UR_FACE);
            int relationsColIndex = cursor.getColumnIndex(DBHelper.RELATIONS);
            int contactInfoColIndex = cursor.getColumnIndex(DBHelper.CONTACT_INFO);
            int emailColIndex = cursor.getColumnIndex(DBHelper.EMAIL);
            int jurAddressColIndex = cursor.getColumnIndex(DBHelper.JUR_ADDRESS);
            int siteColIndex = cursor.getColumnIndex(DBHelper.SITE);
            Contragent contragent = null;
            do {
                contragent = new Contragent();
                contragent.setId(cursor.getString(idColIndex));
                contragent.setUid(cursor.getString(uidColIndex));
                contragent.setNameContragent(cursor.getString(nameColIndex));
                contragent.setInn(cursor.getString(innColIndex));
                contragent.setCode(cursor.getString(codeColIndex));
                contragent.setUrFace(cursor.getString(jurFaceColIndex));
                contragent.setRelations(cursor.getString(relationsColIndex));
                contragent.setContactInfo(cursor.getString(contactInfoColIndex));
                contragent.setEmail(cursor.getString(emailColIndex));
                contragent.setJurAddress(cursor.getString(jurAddressColIndex));
                try {
                    contragent.setSite(cursor.getString(siteColIndex));
                } catch(IllegalStateException e) {
                    contragent.setSite("");
                }
                contragentList.add(contragent);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return contragentList;
    }

    public Contragent getContragent(String id) {
        Contragent contragent = new Contragent();
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = mDb.query(DBHelper.TABLE_CONTRAGENT, null, DBHelper.ID + " = i?",
                new String[] {id}, null, null, null);
        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex(DBHelper.ID);
            int uidColIndex = cursor.getColumnIndex(DBHelper.UID);
            int nameColIndex = cursor.getColumnIndex(DBHelper.NAME_CONTRAGENT);
            int innColIndex = cursor.getColumnIndex(DBHelper.INN);
            int codeColIndex = cursor.getColumnIndex(DBHelper.CODE);
            int jurFaceColIndex = cursor.getColumnIndex(DBHelper.UR_FACE);
            int relationsColIndex = cursor.getColumnIndex(DBHelper.RELATIONS);
            int contactInfoColIndex = cursor.getColumnIndex(DBHelper.CONTACT_INFO);
            int emailColIndex = cursor.getColumnIndex(DBHelper.EMAIL);
            int jurAddressColIndex = cursor.getColumnIndex(DBHelper.JUR_ADDRESS);
            int siteColIndex = cursor.getColumnIndex(DBHelper.SITE);

            contragent.setId(cursor.getString(idColIndex));
            contragent.setUid(cursor.getString(uidColIndex));
            contragent.setNameContragent(cursor.getString(nameColIndex));
            contragent.setInn(cursor.getString(innColIndex));
            contragent.setCode(cursor.getString(codeColIndex));
            contragent.setUrFace(cursor.getString(jurFaceColIndex));
            contragent.setRelations(cursor.getString(relationsColIndex));
            contragent.setContactInfo(cursor.getString(contactInfoColIndex));
            contragent.setEmail(cursor.getString(emailColIndex));
            contragent.setJurAddress(cursor.getString(jurAddressColIndex));
            contragent.setSite(cursor.getString(siteColIndex));
        } else {
            contragent = null;
        }
        mDb.close();
        return contragent;
    }

    public void updateContragent(Contragent contragent) {
        mDb = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.UID, contragent.getUid());
        mDb.update(DBHelper.TABLE_CONTRAGENT, cv, DBHelper.ID + "=?", new String[] {contragent.getId()});
    }

    public void addEventToDB(Event event) {
        mDb = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.EVENT_USER, event.getUser());
        cv.put(DBHelper.EVENT_TIME_BEGIN, event.getTimeBegin());
        cv.put(DBHelper.EVENT_TIME_END, event.getTimeEnd());
        cv.put(DBHelper.EVENT_DATE, event.getDate());
        cv.put(DBHelper.EVENT_MESSAGE, event.getMessage());
        mDb.insert(DBHelper.TABLE_EVENTS, null, cv);
        mDb.close();
    }

    public List<Event> getEvent() {
        List<Event> events = new ArrayList<>();
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = mDb.query(DBHelper.TABLE_EVENTS, null, null, null, null, null, DBHelper.EVENT_TIME_BEGIN + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setUser(cursor.getString(cursor.getColumnIndex(DBHelper.EVENT_USER)));
                event.setTimeBegin(cursor.getLong(cursor.getColumnIndex(DBHelper.EVENT_TIME_BEGIN)));
                event.setTimeEnd(cursor.getLong(cursor.getColumnIndex(DBHelper.EVENT_TIME_END)));
                event.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.EVENT_DATE)));
                event.setMessage(cursor.getString(cursor.getColumnIndex(DBHelper.EVENT_MESSAGE)));
                Log.d("myLog", event.toString());
                events.add(event);
            } while (cursor.moveToNext());
        }
        return events;
    }

    public void addCall(Call call) {
        mDb = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.CALL_PHONE, call.getPhone());
        cv.put(DBHelper.CALL_LOGIN, call.getLogin());
        cv.put(DBHelper.CALL_TIME, call.getTime());
        cv.put(DBHelper.CALL_TYPE, call.getType());
        cv.put(DBHelper.CALL_SEND, call.isSend() ? 1 : 0);
        mDb.insert(DBHelper.TABLE_CALL, null, cv);
        mDb.close();
    }

    public void updateCall(Call call) {
        mDb = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.CALL_PHONE, call.getPhone());
        cv.put(DBHelper.CALL_LOGIN, call.getLogin());
        cv.put(DBHelper.CALL_TIME, call.getTime());
        cv.put(DBHelper.CALL_TYPE, call.getType());
        cv.put(DBHelper.CALL_SEND, 1);
        String time = String.valueOf(call.getTime());
        mDb.update(DBHelper.TABLE_CALL, cv, DBHelper.CALL_TIME + " = ?", new String[] {time});
        mDb.close();
    }

    public List<Call> getCallList() {
        List<Call> calls = new ArrayList<>();
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = mDb.query(DBHelper.TABLE_CALL, null, null, null, null, null, DBHelper.CALL_TIME + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Call call = new Call();
                call.setPhone(cursor.getString(cursor.getColumnIndex(DBHelper.CALL_PHONE)));
                call.setLogin(cursor.getString(cursor.getColumnIndex(DBHelper.CALL_LOGIN)));
                call.setTime(cursor.getLong(cursor.getColumnIndex(DBHelper.CALL_TIME)));
                call.setType(cursor.getInt(cursor.getColumnIndex(DBHelper.CALL_TYPE)));
                call.setSend(cursor.getInt(cursor.getColumnIndex(DBHelper.CALL_SEND)) == 0 ? false : true);
                calls.add(call);
            } while (cursor.moveToNext());
        }
        return calls;
    }
}
