package ru.bda.icrm.model;

/**
 * Created by User on 27.10.2016.
 */

public class PriceSum extends Price {

    private int sum = 1;
    private int totlalCoast = 1;

    public PriceSum() {
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getTotlalCoast() {
        return totlalCoast;
    }

    public void setTotlalCoast(int totlalCoast) {
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
