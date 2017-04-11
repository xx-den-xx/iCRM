package ru.bda.icrm.model;

public class LocationObject {

    private double lng;
    private double lat;
    private long time;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "LocationObject{" +
                "lng=" + lng +
                ", lat=" + lat +
                ", time=" + time +
                '}';
    }
}
