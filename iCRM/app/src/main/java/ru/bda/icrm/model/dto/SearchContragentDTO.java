package ru.bda.icrm.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchContragentDTO {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("string")
    @Expose
    private String string;

    public SearchContragentDTO(String token, String string) {
        this.token = token;
        this.string = string;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
