package ru.bda.icrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationObject {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("lng")
    @Expose
    private double lng;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("time")
    @Expose
    private long time;

    public LocationObject() {
    }

    public LocationObject(String token, double lng, double lat, long time) {
        this.token = token;
        this.lng = lng;
        this.lat = lat;
        this.time = time;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
