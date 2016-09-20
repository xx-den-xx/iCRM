package ru.bda.icrm.model;

/**
 * Created by User on 15.09.2016.
 */
public class Phone {

    private String mId = "";
    private String mContactsId = "";
    private int mType = 0;//если 0 - рабочий, если 1 - мобильный
    private String mNumber = "";

    public Phone() {

    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getContactsId() {
        return mContactsId;
    }

    public void setContactsId(String mContactsId) {
        this.mContactsId = mContactsId;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setmNumber(String mNumber) {
        this.mNumber = mNumber;
    }
}
