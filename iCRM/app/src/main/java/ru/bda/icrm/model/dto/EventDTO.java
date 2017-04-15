package ru.bda.icrm.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.bda.icrm.model.Event;

public class EventDTO {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("timeBegin")
    @Expose
    private long timeBegin;

    @SerializedName("timeEnd")
    @Expose
    private long timeEnd;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("message")
    @Expose
    private String message;

    public EventDTO() {}

    public EventDTO(String token, int id, String user, long timeBegin, long timeEnd, String date, String message) {
        this.token = token;
        this.id = id;
        this.user = user;
        this.timeBegin = timeBegin;
        this.timeEnd = timeEnd;
        this.date = date;
        this.message = message;
    }

    public EventDTO(Event event, String token) {
        this.token = token;
        this.id = event.getId();
        this.user = event.getUser();
        this.timeBegin = event.getTimeBegin();
        this.timeEnd = event.getTimeEnd();
        this.date = event.getDate();
        this.message = event.getMessage();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(long timeBegin) {
        this.timeBegin = timeBegin;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "token='" + token + '\'' +
                ", id=" + id +
                ", user='" + user + '\'' +
                ", timeBegin=" + timeBegin +
                ", timeEnd=" + timeEnd +
                ", date='" + date + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
