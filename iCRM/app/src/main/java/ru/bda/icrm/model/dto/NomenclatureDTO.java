package ru.bda.icrm.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NomenclatureDTO {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("base_id")
    @Expose
    private String baseId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("nomenclature")
    @Expose
    private String nomenclature;
    @SerializedName("parent")
    @Expose
    private String parent;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("ostatok")
    @Expose
    private String ostatok;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("isgroup")
    @Expose
    private String isgroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(String nomenclature) {
        this.nomenclature = nomenclature;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOstatok() {
        return ostatok;
    }

    public void setOstatok(String ostatok) {
        this.ostatok = ostatok;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getIsgroup() {
        return isgroup;
    }

    public void setIsgroup(String isgroup) {
        this.isgroup = isgroup;
    }
}
