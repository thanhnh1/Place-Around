package com.nguyenthanh.placearound.model_weather;

public class Temp {
    private double day;

    private double min;

    private double max;

    private double night;

    private double eve;

    private double morn;

    public double getDay() {
        return day;
    }

    public void setDay(double dat) {
        this.day = day;
    }

    public double getEve() {
        return eve;
    }

    public void setEve(double eve) {
        this.eve = eve;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMorn() {
        return morn;
    }

    public void setMorn(double morn) {
        this.morn = morn;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }

}
