package ru.bda.icrm.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TakeNomenclatureDTO {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("parent")
    @Expose
    private String parent;

    @SerializedName("start")
    @Expose
    private int start;

    @SerializedName("count")
    @Expose
    private int count;


    public TakeNomenclatureDTO(String token, String parent, int start, int count) {
        this.token = token;
        this.parent = parent;
        this.start = start;
        this.count = count;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
