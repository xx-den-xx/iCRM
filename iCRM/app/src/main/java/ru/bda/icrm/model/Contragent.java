package ru.bda.icrm.model;

/**
 * Created by User on 27.06.2016.
 */
public class Contragent {

    public String mUid;//uid контрагента
    public String mCode;//код
    public String mInn;//инн
    public String mCodePoOkpo;//КодПоОКПО
    public String mUrFace;//юр/физ лицо (1/0)
    public String mNameContragent;//наименование контрагента

    public Contragent(String mUid, String mCode, String mInn, String mCodePoOkpo, String mUrFace, String mNameContragent) {
        this.mUid = mUid;
        this.mCode = mCode;
        this.mInn = mInn;
        this.mCodePoOkpo = mCodePoOkpo;
        this.mUrFace = mUrFace;
        this.mNameContragent = mNameContragent;
    }

    public Contragent() {
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String mUid) {
        this.mUid = mUid;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    public String getInn() {
        return mInn;
    }

    public void setInn(String mInn) {
        this.mInn = mInn;
    }

    public String getCodePoOkpo() {
        return mCodePoOkpo;
    }

    public void setCodePoOkpo(String mCodePoOkpo) {
        this.mCodePoOkpo = mCodePoOkpo;
    }

    public String getUrFace() {
        return mUrFace;
    }

    public void setUrFace(String mUrFace) {
        this.mUrFace = mUrFace;
    }

    public String getNameContragent() {
        return mNameContragent;
    }

    public void setNameContragent(String mNameContragent) {
        this.mNameContragent = mNameContragent;
    }
}
