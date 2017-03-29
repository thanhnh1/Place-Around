package com.nguyenthanh.placearound.model_weather;

import java.util.List;

public class ListItem {
    private Temp temp;

    private long dt;

    private double pressure;

    private double humidity;

    private List<WeatherItem> weather;

    private double speed;

    private double deg;

    private double clouds;

    private double rain;

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public Temp getTemp() {
        return temp;
    }

    public double getClouds() {
        return clouds;
    }

    public double getDeg() {
        return deg;
    }

    public long getDt() {
        return dt;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getRain() {
        return rain;
    }

    public double getSpeed() {
        return speed;
    }

    public void setClouds(double clouds) {
        this.clouds = clouds;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public List<WeatherItem> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherItem> weather) {
        this.weather = weather;
    }
}
