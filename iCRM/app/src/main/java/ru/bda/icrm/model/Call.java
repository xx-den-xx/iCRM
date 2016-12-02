package ru.bda.icrm.model;

/**
 * Created by User on 17.11.2016.
 */

public class Call {

    private String phone = "";
    private String login = "";
    private int type = 0;
    private long time = 0;
    private boolean send = false;

    public Call() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }



    @Override
    public String toString() {
        return "Call{" +
                "phone='" + phone + '\'' +
                ", login='" + login + '\'' +
                ", type=" + type +
                ", time=" + time +
                ", send=" + send +
                '}';
    }
}
