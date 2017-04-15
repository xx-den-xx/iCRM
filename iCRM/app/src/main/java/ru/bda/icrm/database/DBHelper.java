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
    public static final String TABLE_EVENTS = "table_events";
    public static final String TABLE_CALL = "table_call";

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

    public static final String CONTACTS_CONTRAGENT_ID = "contragent_id";
    public static final String CONTACTS_NAME = "contacts_name";
    public static final String CONTACTS_ROLE = "contacts_role";
    public static final String CONTACTS_COMMENTS = "contacts_comments";
    public static final String CONTACTS_WORK_PHONE = "contacts_work_phone";
    public static final String CONTACTS_MOBILE_PHONE = "contacts_mobile_phone";
    public static final String CONTACTS_EMAIL = "contacts_email";

    public static final String PHONE_TYPE = "phone_type";
    public static final String PHONE_CONTRAGENT_ID = "phone_contragent_id";
    public static final String PHONE_NUMBER = "phone_number";

    //поля для таблицы TABLE_EVENTS
    public static final String EVENT_ID_SERVER = "id_server";
    public static final String EVENT_USER = "user";
    public static final String EVENT_TIME_BEGIN = "time_begin";
    public static final String EVENT_TIME_END = "time_end";
    public static final String EVENT_DATE = "date";
    public static final String EVENT_MESSAGE = "message";

    //поля для тфблицы вызовов
    public static final String CALL_PHONE = "phone";
    public static final String CALL_LOGIN = "login";
    public static final String CALL_TYPE = "type";
    public static final String CALL_TIME = "time";
    public static final String CALL_DURATION = "duration";
    public static final String CALL_SEND = "send";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTRAGENT
                + "(" + ID + " integer primary key, "
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

        db.execSQL("create table " + TABLE_CONTACTS
                + "(" + ID + " integer primary key, "
                + CONTACTS_CONTRAGENT_ID + " text, "
                + CONTACTS_NAME + " varchar, "
                + CONTACTS_ROLE + " text, "
                + CONTACTS_COMMENTS + " text, "
                + CONTACTS_WORK_PHONE + " text, "
                + CONTACTS_MOBILE_PHONE + " text, "
                + CONTACTS_EMAIL + " text);");

        db.execSQL("create table " + TABLE_TELEPHONES
                + "(" + ID + " integer primary key, "
                + PHONE_CONTRAGENT_ID + " text, "
                + PHONE_TYPE + " text, "
                + PHONE_NUMBER + " text);");

        db.execSQL("create table " + TABLE_EVENTS
                + "(" + ID + " integer primary key, "
                + EVENT_ID_SERVER + " integer, "
                + EVENT_USER + " text, "
                + EVENT_TIME_BEGIN + " real, "
                + EVENT_TIME_END + " real, "
                + EVENT_DATE + " text, "
                + EVENT_MESSAGE + " text);");

        db.execSQL("create table " + TABLE_CALL
                + "(" + ID + " integer primary key, " +
                CALL_PHONE + " text, " +
                CALL_LOGIN + " text, " +
                CALL_TIME + " real, " +
                CALL_TYPE + " text, " +
                CALL_DURATION + " text, " +
                CALL_SEND + " integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
