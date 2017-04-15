package ru.bda.icrm.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NomenclatureDataDTO {

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("nomenclature")
    @Expose
    private List<NomenclatureDTO> nomenclature = null;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<NomenclatureDTO> getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(List<NomenclatureDTO> nomenclature) {
        this.nomenclature = nomenclature;
    }
}
