package ru.bda.icrm.model;

/**
 * Created by User on 13.09.2016.
 */
public class Clients {
    private String mId;
    private String mContragentId;
    private String mName;
    private String mRole;
    private String mComments;
    private Phone mWorkPhone;
    private Phone mMobilePhone;
    private String mWorkEmail;

    public Clients() {}

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

    public Phone getWorkPhone() {
        return mWorkPhone;
    }

    public void setWorkPhone(Phone mWorkPhone) {
        this.mWorkPhone = mWorkPhone;
    }

    public Phone getMobilePhone() {
        return mMobilePhone;
    }

    public void setMobilePhone(Phone mMobilePhone) {
        this.mMobilePhone = mMobilePhone;
    }

    public String getWorkEmail() {
        return mWorkEmail;
    }

    public void setWorkEmail(String mWorkEmail) {
        this.mWorkEmail = mWorkEmail;
    }

    public void addToDB(Clients clients) {

    }

    @Override
    public String toString() {
        return "Clients{" +
                "mName='" + mName + '\'' +
                ", mRole='" + mRole + '\'' +
                ", mComments='" + mComments + '\'' +
                ", mWorkPhone='" + mWorkPhone + '\'' +
                ", mMobilePhone='" + mMobilePhone + '\'' +
                ", mWorkEmail='" + mWorkEmail + '\'' +
                '}';
    }
}