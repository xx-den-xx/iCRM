package ru.bda.icrm.model;

import java.util.ArrayList;
import java.util.List;

public class ClientObject {

    private int id = 0;
    private String name = "";
    private String contact = "";
    private String comments = "";
    private String address = "";
    private String phone = "";
    private double lat = 0;
    private double lon = 0;
    private List<Photo> photoUrl = new ArrayList<>();

    public ClientObject() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public List<Photo> getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(List<Photo> photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "ClientObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", comments='" + comments + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", photoUrl=" + photoUrl +
                '}';
    }
}
