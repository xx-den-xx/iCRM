package ru.bda.icrm.model;

import java.util.Date;

/**
 * Created by User on 06.10.2016.
 */

public class Event {
    private String user;
    private long timeBegin;
    private long timeEnd;
    private String date;
    private String message;

    public Event() {}

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

    @Override
    public String toString() {
        return "Event{" +
                "user='" + user + '\'' +
                ", timeBegin=" + timeBegin +
                ", timeEnd=" + timeEnd +
                ", date='" + date + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
