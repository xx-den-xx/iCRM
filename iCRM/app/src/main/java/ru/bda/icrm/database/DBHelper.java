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
    public static final String TABLE_TELEPHONES = "table_telephones";
    public static final String TABLE_CONTACTS = "table_contacts";

    public static final String ID = "_id";
    public static final String UID = "uid";//uid контрагента
    public static final String CODE = "code";//код
    public static final String INN = "inn";//инн
    public static final String CODE_PO_OKPO = "code_po_okpo";//КодПоОКПО
    public static final String UR_FACE = "ur_face";//юр/физ лицо (1/0)
    public static final String NAME_CONTRAGENT = "name_contragent";//наименование контрагента
    public static final String RELATIONS = "relations";//вид отношений (1/0)
    public static final String CONTACT_INFO = "contact_info";//контактная информация
    public static final String EMAIL = "email";//почта
    public static final String JUR_ADDRESS = "jur_address";//юридический адрес
    public static final String SITE = "site";//сайт

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTRAGENT
                + "(" + ID + " integer primary key autoincrement, "
                + UID + " varchar, "
                + CODE + " text, "
                + INN + " text, "
                + CODE_PO_OKPO + " text, "
                + UR_FACE + " text, "
                + NAME_CONTRAGENT + " text, "
                + RELATIONS + " text, "
                + CONTACT_INFO + " text,"
                + EMAIL + " text, "
                + JUR_ADDRESS + " text, "
                + SITE + " text);");
        /**db.execSQL("create table " + TABLE_TELEPHONES
                + "(" + ID + " integer primary key autoincrement, "
                + UID + " varchar, "
                + CODE + " text, ");*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
