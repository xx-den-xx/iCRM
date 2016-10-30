package ru.bda.icrm.model;

import java.util.List;

/**
 * Created by User on 27.06.2016.
 */
public class Contragent {

    private String mId = "";//id контрагента
    private String mUid = "";//uid контрагента
    private String mCode = "";//код
    private String mInn = "";//инн
    private String mCodePoOkpo = "";//КодПоОКПО
    private String mUrFace = "";//юр/физ лицо (1/0)
    private String mNameContragent = "";//наименование контрагента
    private String mRelations = "";//вид отношений
    private String mContactInfo = "";//контактная информация
    private String mEmail = "";//почта
    private String mJurAddress = "";//юридический адрес
    private String mFizAddress = "";//физический адрес
    private String mSite = "";//сайт
    private List<String> mPhones = null;//список телефонов
    private List<String> mContacts = null;//список контактных лиц
    private double lat;//latitude
    private double lon;//longitude



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

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
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

    public String getRelations() {
        return mRelations;
    }

    public void setRelations(String mRelations) {
        this.mRelations = mRelations;
    }

    public String getContactInfo() {
        return mContactInfo;
    }

    public void setContactInfo(String mContactInfo) {
        this.mContactInfo = mContactInfo;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getJurAddress() {
        return mJurAddress;
    }

    public void setJurAddress(String mJurAddress) {
        this.mJurAddress = mJurAddress;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String mSite) {
        this.mSite = mSite;
    }

    public List<String> getPhones() {
        return mPhones;
    }

    public void setPhones(List<String> mPhones) {
        this.mPhones = mPhones;
    }

    public List<String> getContacts() {
        return mContacts;
    }

    public void setContacts(List<String> mContacts) {
        this.mContacts = mContacts;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getmFizAddress() {
        return mFizAddress;
    }

    public void setmFizAddress(String mFizAddress) {
        this.mFizAddress = mFizAddress;
    }

    @Override
    public String toString() {
        return "Contragent{" +
                "mId='" + mId + '\'' +
                ", mUid='" + mUid + '\'' +
                ", mCode='" + mCode + '\'' +
                ", mInn='" + mInn + '\'' +
                ", mCodePoOkpo='" + mCodePoOkpo + '\'' +
                ", mUrFace='" + mUrFace + '\'' +
                ", mNameContragent='" + mNameContragent + '\'' +
                ", mRelations='" + mRelations + '\'' +
                ", mContactInfo='" + mContactInfo + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mJurAddress='" + mJurAddress + '\'' +
                ", mSite='" + mSite + '\'' +
                ", mPhones=" + mPhones +
                ", mContacts=" + mContacts +
                '}';
    }
}
