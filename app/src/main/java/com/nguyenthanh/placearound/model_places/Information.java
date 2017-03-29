package com.nguyenthanh.placearound.model_places;

/**
 * Created by Administrator on 3/29/2017.
 */

public class Information {

    public String address;

    public String name;
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Information() {

    }

    public Information(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
