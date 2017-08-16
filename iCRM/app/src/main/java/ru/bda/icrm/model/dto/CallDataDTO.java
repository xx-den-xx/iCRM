package ru.bda.icrm.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CallDataDTO {

    @SerializedName("token")
    @Expose
    String token;

    @SerializedName("manager_id")
    @Expose
    String manager_id;

    @SerializedName("call")
    @Expose
    List<CallDTO> callList;

    public CallDataDTO() {

    }

    public CallDataDTO(String token, String manager_id, List<CallDTO> callList) {
        this.token = token;
        this.manager_id = manager_id;
        this.callList = callList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<CallDTO> getCallList() {
        return callList;
    }

    public void setCallList(List<CallDTO> callList) {
        this.callList = callList;
    }
}
