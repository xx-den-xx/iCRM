package ru.bda.icrm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 27.06.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DB_I_CRM";

    public static final String TABLE_CONTRAGENT = "table_contragent";

    public static final String UID = "uid";//uid контрагента
    public static final String CODE = "code";//код
    public static final String INN = "inn";//инн
    public static final String CODE_PO_OKPO = "code_po_okpo";//КодПоОКПО
    public static final String UR_FACE = "ur_face";//юр/физ лицо (1/0)
    public static final String NAME_CONTRAGENT = "name_contragent";//наименование контрагента

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTRAGENT
                + "(" + UID + " varchar primary key autoincrement, "
                + CODE + " text, "
                + INN + " integer, "
                + CODE_PO_OKPO + " integer, "
                + UR_FACE + " integer, "
                + NAME_CONTRAGENT + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
