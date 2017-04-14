package ru.bda.icrm.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContragentDataDTO {

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("data")
    @Expose
    private List<ContragentItemDTO> data = null;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ContragentItemDTO> getData() {
        return data;
    }

    public void setData(List<ContragentItemDTO> data) {
        this.data = data;
    }
}
