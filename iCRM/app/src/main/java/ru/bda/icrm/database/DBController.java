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
}
