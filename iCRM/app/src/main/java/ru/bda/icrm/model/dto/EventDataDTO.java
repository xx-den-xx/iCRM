package ru.bda.icrm.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventDataDTO {
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("data")
    @Expose
    private List<EventDTO> data = null;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<EventDTO> getData() {
        return data;
    }

    public void setData(List<EventDTO> data) {
        this.data = data;
    }
}
