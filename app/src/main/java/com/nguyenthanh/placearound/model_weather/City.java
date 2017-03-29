package com.nguyenthanh.placearound.model_weather;

public class City {
    private double id;

    private String name;

    private Coord coord;

    private String country;

    private double population;

    public Coord getCoord() {
        return coord;
    }

    public double getId() {
        return id;
    }

    public double getPopulation() {
        return population;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setId(double id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPopulation(double population) {
        this.population = population;
    }
}
