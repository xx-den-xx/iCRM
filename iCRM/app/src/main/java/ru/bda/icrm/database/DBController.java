package ru.bda.icrm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.model.Contragent;

/**
 * Created by User on 28.06.2016.
 */
public class DBController {

    private static volatile DBController instance;
    private Context context;
    private DBHelper dbHelper;
    private ContentValues mContentValues;
    private SQLiteDatabase mDb;

    private DBController(Context context) {
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
                    isHaveUID = cursor.getString(idColIndex).equals(contragent.getUid());
                } while (cursor.moveToNext());
                if (!isHaveUID) {
                    mContentValues.put(DBHelper.UID, contragent.getUid());
                    mContentValues.put(DBHelper.NAME_CONTRAGENT, contragent.getNameContragent());
                    if (mDb != null) mDb.insert(DBHelper.TABLE_CONTRAGENT, null, mContentValues);
                }
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
            int uidColIndex = cursor.getColumnIndex(DBHelper.UID);
            int nameColIndex = cursor.getColumnIndex(DBHelper.NAME_CONTRAGENT);
            Contragent contragent = null;
            do {
                contragent = new Contragent();
                contragent.setUid(cursor.getString(uidColIndex));
                contragent.setNameContragent(cursor.getString(nameColIndex));
                contragentList.add(contragent);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return contragentList;
    }
}
