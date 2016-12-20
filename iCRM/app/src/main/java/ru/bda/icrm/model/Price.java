package ru.bda.icrm.model;

/**
 * Created by User on 12.10.2016.
 */

public class Price {

    private int id = 0;
    private String code = "";
    private String parent = "";
    private String title = "";
    private String unit = "";
    private boolean isGroup = false;
    private double price = 0;

    public Price() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", parent='" + parent + '\'' +
                ", title='" + title + '\'' +
                ", unit='" + unit + '\'' +
                ", isGroup=" + isGroup +
                ", price=" + price +
                '}';
    }
}
