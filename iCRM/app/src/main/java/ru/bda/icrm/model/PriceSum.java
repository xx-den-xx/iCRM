package ru.bda.icrm.model;

import java.io.Serializable;

/**
 * Created by User on 27.10.2016.
 */

public class PriceSum extends Price implements Serializable{

    private int number = 1;
    private double totlalCoast = 1;

    public PriceSum() {
    }

    public int getSum() {
        return number;
    }

    public void setSum(int sum) {
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
