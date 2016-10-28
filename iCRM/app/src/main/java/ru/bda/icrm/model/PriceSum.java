package ru.bda.icrm.model;

/**
 * Created by User on 27.10.2016.
 */

public class PriceSum extends Price {

    private double sum = 1;
    private double totlalCoast = 1;

    public PriceSum() {
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
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
                "sum=" + sum +
                ", totlalCoast=" + totlalCoast +
                '}';
    }
}
