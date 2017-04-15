package ru.bda.icrm.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

public class TakeContragentListDTO {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("start")
    @Expose
    private int start;

    @SerializedName("count")
    @Expose
    private int count;

    public TakeContragentListDTO(String token, int start, int count) {
        this.token = token;
        this.start = start;
        this.count = count;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
