package ru.bda.icrm.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.bda.icrm.model.Call;

public class CallDTO {

    @SerializedName("phone")
    @Expose
    String phone = "";

    @SerializedName("login")
    @Expose
    String login = "";

    @SerializedName("type")
    @Expose
    String type = "";

    @SerializedName("time")
    @Expose
    long time = 0;

    @SerializedName("duration")
    @Expose
    String duration = "";

    public CallDTO(Call call) {
        this.phone = call.getPhone();
        this.login = call.getLogin();
        this.type = call.getType();
        this.time = call.getTime();
        this.duration = call.getDuration();
    }

    public CallDTO(String phone, String login, String type, long time, String duration) {
        this.phone = phone;
        this.login = login;
        this.type = type;
        this.time = time;
        this.duration = duration;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "CallDTO{" +
                "phone='" + phone + '\'' +
                ", login='" + login + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", duration='" + duration + '\'' +
                '}';
    }
}
