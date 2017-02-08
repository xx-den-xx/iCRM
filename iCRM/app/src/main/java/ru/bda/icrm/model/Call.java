package ru.bda.icrm.model;

/**
 * Created by User on 17.11.2016.
 */

public class Call {

    private String phone = "";
    private String login = "";
    private String type = "";
    private long time = 0;
    private String duration = "";
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Call{" +
                "phone='" + phone + '\'' +
                ", login='" + login + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", duration='" + duration + '\'' +
                ", send=" + send +
                '}';
    }
}
