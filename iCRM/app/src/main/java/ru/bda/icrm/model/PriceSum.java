package ru.bda.icrm.model;

import java.io.Serializable;

/**
 * Created by User on 27.10.2016.
 */

public class PriceSum extends Price implements Serializable{

    private double number = 1;
    private double totlalCoast = 1;

    public PriceSum() {
    }

    public double getSum() {
        return number;
    }

    public void setSum(double sum) {
        this.number = sum;
    }

    public double getTotalCoast() {
        return totlalCoast;
    }

    public void setTotlalCoast(double totlalCoast) {
        this.totlalCoast = totlalCoast;
    }

    @Override
    public String toString() {
        return "PriceSum{" +
                "sum=" + number +
                ", totlalCoast=" + totlalCoast +
                '}';
    }
}
