package com.nguyenthanh.placearound.model_places;

import com.google.api.client.util.Key;

import java.io.Serializable;

public class Place implements Serializable {

    private static final String ZERO = null;

    public static class Geometry implements Serializable {
        @Key
        public Location location;
    }

    public static class Location implements Serializable {
        @Key
        public double lat;

        @Key
        public double lng;
    }

    @Key
    public String id;

    @Key
    public String name;

    @Key
    public String reference;

    @Key
    public String icon;

    @Key
    public String vicinity;

    @Key
    public Geometry geometry;

    @Key
    public String formattedAddress;

    @Key
    public String formattedPhoneNumber;

    @Override
    public String toString() {
        return name + " - " + id + " - " + reference;
    }
}
