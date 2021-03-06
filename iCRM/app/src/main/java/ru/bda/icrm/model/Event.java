package ru.bda.icrm.model;

import ru.bda.icrm.model.dto.EventDTO;

public class Event {
    private int id;
    private String user;
    private long timeBegin;
    private long timeEnd;
    private String date;
    private String message;
    private boolean isFirstDay = false;
    private boolean isNowDay = false;

    public Event() {}

    public Event(EventDTO event) {
        this.id = event.getId();
        this.user = event.getUser();
        this.timeBegin = event.getTimeBegin();
        this.timeEnd = event.getTimeEnd();
        this.date = event.getDate();
        this.message = event.getMessage();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isFirstDay() {
        return isFirstDay;
    }

    public void setFirstDay(boolean firstDay) {
        isFirstDay = firstDay;
    }

    public boolean isNowDay() {
        return isNowDay;
    }

    public void setNowDay(boolean nowDay) {
        isNowDay = nowDay;
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
