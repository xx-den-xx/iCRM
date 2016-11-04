package ru.bda.icrm.model;

/**
 * Created by User on 13.09.2016.
 */
public class Contact {
    private String mId = "";
    private String mContragentId = "";
    private String mName = "";
    private String mRole = "";
    private String mComments = "";
    private String mWorkPhone = "";
    private String mMobilePhone = "";
    private String mWorkEmail = "";

    public Contact() {}

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getContragentId() {
        return mContragentId;
    }

    public void setContragentId(String mContragentId) {
        this.mContragentId = mContragentId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getRole() {
        return mRole;
    }

    public void setRole(String mRole) {
        this.mRole = mRole;
    }

    public String getComments() {
        return mComments;
    }

    public void setComments(String mComments) {
        this.mComments = mComments;
    }

    public String getWorkPhone() {
        return mWorkPhone;
    }

    public void setWorkPhone(String mWorkPhone) {
        this.mWorkPhone = mWorkPhone;
    }

    public String getMobilePhone() {
        return mMobilePhone;
    }

    public void setMobilePhone(String mMobilePhone) {
        this.mMobilePhone = mMobilePhone;
    }

    public String getWorkEmail() {
        return mWorkEmail;
    }

    public void setWorkEmail(String mWorkEmail) {
        this.mWorkEmail = mWorkEmail;
    }

    public void addToDB(Contact clients) {

    }

    @Override
    public String toString() {
        return "Contact{" +
                "mId='" + mId + '\'' +
                ", mContragentId='" + mContragentId + '\'' +
                ", mName='" + mName + '\'' +
                ", mRole='" + mRole + '\'' +
                ", mComments='" + mComments + '\'' +
                ", mWorkPhone='" + mWorkPhone + '\'' +
                ", mMobilePhone='" + mMobilePhone + '\'' +
                ", mWorkEmail='" + mWorkEmail + '\'' +
                '}';
    }
}